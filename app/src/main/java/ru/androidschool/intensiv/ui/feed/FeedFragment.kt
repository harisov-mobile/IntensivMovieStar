package ru.androidschool.intensiv.ui.feed

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.onTextChangedObservable
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FeedFragment : Fragment(R.layout.feed_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

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

        // Получение данных из API:
        val singleNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies()
        val singleUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        val singlePopularMovies = MovieApiClient.apiClient.getPopularMovies()

        // Получаем список фильмов для "Рекомендуем"
        val disposableNowPlayingMovies = singleNowPlayingMovies
            .applySchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                    response ->
                    response?.let { response ->
                        val movieResultList = response.results
                        val moviesList = getMainCardContainerList(R.string.recommended, movieResultList)
                        adapter.apply { addAll(moviesList) }
                    }
            },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении NowPlayingMovies")
                }
            )

        compositeDisposable.add(disposableNowPlayingMovies)

        // Получаем список фильмов для "Рекомендуем"
        val disposableUpcomingMovies = singleUpcomingMovies
            .applySchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                    response ->
                    response?.let { response ->
                        val movieResultList = response.results
                        val moviesList = getMainCardContainerList(R.string.upcoming, movieResultList)
                        adapter.apply { addAll(moviesList) }
                    }
            },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении UpcomingMovies")
                }
            )

        compositeDisposable.add(disposableUpcomingMovies)

        // Получаем список фильмов для "Популярные"
        val disposablePopularMovies = singlePopularMovies
            .applySchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                    response ->
                    response?.let { response ->
                        val movieResultList = response.results
                        val moviesList = getMainCardContainerList(R.string.popular, movieResultList)
                        adapter.apply { addAll(moviesList) }
                    }
            },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении PopularMovies")
                }
            )

        compositeDisposable.add(disposablePopularMovies)
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(KEY_MOVIE_ID, movie.id)
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

        // при клике на фильм и возврате в список
        // происходило добавление MainCardContainer в адаптер, в результате чего задваивались списки фильмов
        // поэтому очищаю
        adapter.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear() // диспозабл освободить!
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    private fun getMainCardContainerList(title: Int, movieResultList: List<Movie>): List<MainCardContainer> {
        val moviesList = listOf(
            MainCardContainer(
                title,
                movieResultList.map {
                    MovieItem(it) { movie ->
                        openMovieDetails(
                            movie
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
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.trim() } // удалить все пробелы
            .filter { it.length > MIN_LENGTH } // Длина слова должна быть > 3 символов
            .debounce(SEARCH_DELAY_MILLISEC, TimeUnit.MILLISECONDS) // Отправлять введёное слово не раньше 0.5 секунды с момента окончания ввода
            .subscribe(
                { searchString ->
                    searchString?.let {
                        openSearch(searchString)
                    }
                },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при поиске")
                }
            )

        compositeDisposable.add(searchDisposable)
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_MOVIE_ID = "movie_id"
        const val KEY_SEARCH = "search"
        const val SEARCH_DELAY_MILLISEC = 500L
    }
}
