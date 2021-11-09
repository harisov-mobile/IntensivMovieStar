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
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function3
import kotlinx.android.synthetic.main.feed_fragment.*
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.search_toolbar.view.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.applyProgressBar
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.onTextChangedObservable
import ru.androidschool.intensiv.utils.Const
import timber.log.Timber
import java.util.*
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

        // Получение данных из API и вывод этих данных "одним махом", используя zip:
        val singleNowPlayingMovies = MovieApiClient.apiClient.getNowPlayingMovies()
        val singleUpcomingMovies = MovieApiClient.apiClient.getUpcomingMovies()
        val singlePopularMovies = MovieApiClient.apiClient.getPopularMovies()

        compositeDisposable.add(
            Single.zip(singleNowPlayingMovies, singleUpcomingMovies, singlePopularMovies,
            Function3<MovieResponse, MovieResponse, MovieResponse, List<List<MainCardContainer>>> {
                nowPlayingMoviesResponse, upcomingMoviesResponse, popularMoviesResponse ->
                var mainCardContainerList = mutableListOf<List<MainCardContainer>>()

                nowPlayingMoviesResponse?.let { response ->
                    mainCardContainerList.add(getMainCardContainerList(R.string.recommended, response.results))
                }
                upcomingMoviesResponse?.let { response ->
                    mainCardContainerList.add(getMainCardContainerList(R.string.upcoming, response.results))
                }
                nowPlayingMoviesResponse?.let { response ->
                    mainCardContainerList.add(getMainCardContainerList(R.string.popular, response.results))
                }

                return@Function3 mainCardContainerList
            })
            .applySchedulers()
            .applyProgressBar(progress_bar)
            .subscribe(
                {
                    // это OnNext
                    mainCardContainerList -> mainCardContainerList.forEach { adapter.apply { addAll(it) } }
                },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении фильмов")
                }
            )
        )
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, movie.id)
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
        const val KEY_SEARCH = "search"
        const val SEARCH_DELAY_MILLISEC = 500L
    }
}
