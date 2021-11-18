package ru.androidschool.intensiv.presentation.feed

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.database.MovieDao
import ru.androidschool.intensiv.data.database.MovieDatabase
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.mappers.MovieMapper
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.vo.MovieVO
import ru.androidschool.intensiv.presentation.applySchedulers
import ru.androidschool.intensiv.ui.applyProgressBar
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.onTextChangedObservable
import ru.androidschool.intensiv.utils.Const
import ru.androidschool.intensiv.utils.ViewFeature
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var movieDao: MovieDao

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable = CompositeDisposable()

        trackSearchInput()

        movies_recycler_view.adapter = adapter

        movieDao = MovieDatabase.get(requireContext()).movieDao()

        val completableCallGetOfflineData = Completable.create {
            getOfflineData()
            it.onComplete()
        }

        val completableCallGetMoviesFromInternet = Completable.create {
            getMoviesFromInternet()
            it.onComplete()
        }

        compositeDisposable.add(completableCallGetOfflineData
            .andThen(completableCallGetMoviesFromInternet)
            .applySchedulers()
            .subscribe())
    }

    private fun getOfflineData() {

        // Получение данных из БД и вывод этих данных "одним махом", используя zip:
        val singleNowPlayingMovies = getMoviesFromDB(ViewFeature.NOW_PLAYING)
        val singleUpcomingMovies = getMoviesFromDB(ViewFeature.UPCOMING)
        val singlePopularMovies = getMoviesFromDB(ViewFeature.POPULAR)

        compositeDisposable.add(
            Single.zip(singleNowPlayingMovies, singleUpcomingMovies, singlePopularMovies,
                Function3<List<MovieAndGenreAndActorAndProductionCompany>, List<MovieAndGenreAndActorAndProductionCompany>, List<MovieAndGenreAndActorAndProductionCompany>, List<List<MainCardContainer>>> {
                        nowPlayingMovies, upcomingMovies, popularMovies ->
                    val mainCardContainerList = mutableListOf<List<MainCardContainer>>()

                    mainCardContainerList.add(getMainCardContainerListFromDB(R.string.recommended, nowPlayingMovies, ViewFeature.NOW_PLAYING))
                    mainCardContainerList.add(getMainCardContainerListFromDB(R.string.upcoming, upcomingMovies, ViewFeature.UPCOMING))
                    mainCardContainerList.add(getMainCardContainerListFromDB(R.string.popular, popularMovies, ViewFeature.POPULAR))

                    return@Function3 mainCardContainerList
                })
                .applySchedulers()
                .applyProgressBar(progress_bar)
                .subscribe(
                    {
                        // это OnNext (учебный комментарий, в реальном проекте такого комментария не будет)
                        mainCardContainerList ->
                        adapter.clear()
                        mainCardContainerList.forEach { adapter.apply { addAll(it) } }
                    },
                    {
                        error -> Timber.e(error, "Ошибка при получении фильмов")
                    }
                )
        )
    }

    private fun getMoviesFromInternet() {

        // Получение данных из API и вывод этих данных "одним махом", используя zip:
        val singleNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies()
        val singleUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        val singlePopularMovies = MovieApiClient.apiClient.getPopularMovies()

        compositeDisposable.add(
            Single.zip(singleNowPlayingMovies, singleUpcomingMovies, singlePopularMovies,
                Function3<MovieResponse, MovieResponse, MovieResponse, List<List<MainCardContainer>>> {
                        nowPlayingMoviesResponse, upcomingMoviesResponse, popularMoviesResponse ->
                    val mainCardContainerList = mutableListOf<List<MainCardContainer>>()

                    mainCardContainerList.add(getMainCardContainerListFromApi(R.string.recommended, nowPlayingMoviesResponse.results, ViewFeature.NOW_PLAYING))
                    saveMovieResultToDB(nowPlayingMoviesResponse.results, ViewFeature.NOW_PLAYING)

                    mainCardContainerList.add(getMainCardContainerListFromApi(R.string.upcoming, upcomingMoviesResponse.results, ViewFeature.UPCOMING))
                    saveMovieResultToDB(upcomingMoviesResponse.results, ViewFeature.UPCOMING)

                    mainCardContainerList.add(getMainCardContainerListFromApi(R.string.popular, popularMoviesResponse.results, ViewFeature.POPULAR))
                    saveMovieResultToDB(popularMoviesResponse.results, ViewFeature.POPULAR)

                    return@Function3 mainCardContainerList
                })
                .applySchedulers()
                .applyProgressBar(progress_bar)
                .subscribe(
                    {
                        // это OnNext (учебный комментарий, в реальном проекте такого комментария не будет)
                        mainCardContainerList ->
                        adapter.clear() // так как данные из интернета успешно получены, можно очистить адаптер от данных, которые получили из БД
                        mainCardContainerList.forEach { adapter.apply { addAll(it) } }
                    },
                    {
                        error -> Timber.e(error, "Ошибка при получении фильмов")
                    }
                )
        )
    }

    private fun openMovieDetails(movieVO: MovieVO) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, movieVO.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    private fun openSearch(searchText: String) {
        val bundle = Bundle()
        bundle.putString(KEY_SEARCH, searchText)
        findNavController().navigate(R.id.search_dest, bundle, options)
    }

    override fun onStop() {
        super.onStop()
        search_toolbar.clear()

        // (учебный комментарий, в реальном проекте такого комментария не будет)
        // при клике на фильм и возврате в список
        // происходило добавление MainCardContainer в адаптер, в результате чего задваивались списки фильмов
        // поэтому очищаю
        adapter.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    private fun getMainCardContainerListFromApi(title: Int, movieResultList: List<Movie>, viewFeature: ViewFeature): List<MainCardContainer> {

        val moviesList = listOf(
            MainCardContainer(
                title,
                movieResultList.map { movie ->
                    MovieMapper.toMovieVO(movie, viewFeature) }
                    .map { movieVO ->
                        MovieItem(movieVO) {
                            openMovieDetails(
                                it
                            )
                        }
                    }
            )
        )
        return moviesList
    }

    private fun saveMovieResultToDB(movieResultList: List<Movie>, viewFeature: ViewFeature) {

        val completableCallDelete = Completable.create {
            deleteViewFeaturedMoviesFromDB(viewFeature)
            it.onComplete()
        }

        val completableCallInsertMoviesToDB = Completable.create {
            insertMoviesToDB(movieResultList, viewFeature)
            it.onComplete()
        }

        // (учебный комментарий, в реальном проекте такого комментария не будет)
        // ВОПРОС:
        // НЕ ЗНАЮ, КАК заставить метод deleteViewFeaturedMoviesFromDB выполнится прежде, чем метод insertMoviesToDB
        // У МЕНЯ ВОЗНИКАЕТ СОСТОЯНИЕ "ГОНКИ", метод insertMoviesToDB иногда опережает deleteViewFeaturedMoviesFromDB
        compositeDisposable.add(completableCallDelete
            .andThen(completableCallInsertMoviesToDB)
            .applySchedulers()
            .subscribe()
        )
    }

    private fun insertMoviesToDB(movieResultList: List<Movie>, viewFeature: ViewFeature) {
        val movieDBOList = movieResultList.map { movie ->
            MovieMapper.toMovieDBO(movie, viewFeature)
        }

        compositeDisposable.add(movieDao.insertMovies(movieDBOList)
            .applySchedulers()
            .subscribe()
        )
    }

    private fun getMainCardContainerListFromDB(title: Int, movieList: List<MovieAndGenreAndActorAndProductionCompany>, viewFeature: ViewFeature): List<MainCardContainer> {

        val moviesList = listOf(
            MainCardContainer(
                title,
                movieList.map { movie ->
                    MovieMapper.toMovieVO(movie.movieDBO, viewFeature) }
                    .map { movieVO ->
                    MovieItem(movieVO) {
                        openMovieDetails(
                            it
                        )
                    }
                }
            )
        )
        return moviesList
    }

    private fun trackSearchInput() {

        val searchDisposable = search_toolbar.search_edit_text
            .onTextChangedObservable()
            .applySchedulers()
            .map { it.trim() } // удалить все пробелы (учебный комментарий, в реальном проекте такого комментария не будет)
            .filter { it.length > MIN_LENGTH } // Длина слова должна быть > 3 символов (учебный комментарий, в реальном проекте такого комментария не будет)
            .debounce(SEARCH_DELAY_MILLISEC, TimeUnit.MILLISECONDS) // Отправлять введёное слово не раньше 0.5 секунды с момента окончания ввода (учебный комментарий, в реальном проекте такого комментария не будет)
            .subscribe(
                { searchString ->
                    searchString?.let {
                        openSearch(searchString)
                    }
                },
                {
                    error -> Timber.e(error, "Ошибка при поиске")
                }
            )

        compositeDisposable.add(searchDisposable)
    }

    private fun getMoviesFromDB(viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>> {
        return movieDao.getMovies(viewFeature)
    }

    private fun deleteViewFeaturedMoviesFromDB(viewFeature: ViewFeature) {
        // удалить записи из БД
        compositeDisposable.add(movieDao.deleteViewFeaturedMovies(viewFeature)
            .applySchedulers()
            .subscribe()
        )
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_SEARCH = "search"
        const val SEARCH_DELAY_MILLISEC = 500L
    }
}
