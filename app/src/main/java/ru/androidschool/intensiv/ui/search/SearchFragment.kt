package ru.androidschool.intensiv.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.progress_bar.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.applyProgressBar
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.feed.MovieItem
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {

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

        // считываем аргументы
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        search_toolbar.setText(searchTerm)

        movies_recycler_view.adapter = adapter

        compositeDisposable = CompositeDisposable()

        searchTerm?.let {
            // Получаем список найденных фильмов
            val singleSearchedMovies = MovieApiClient.apiClient.getSearchedMovies(query = searchTerm)
            val disposableSearchedMovies = singleSearchedMovies
                .applySchedulers()
                .applyProgressBar(progress_bar)
                .subscribe(
                    { // в случае успешного получения данных:
                        response ->
                        response?.let { response ->
                            val movieResultList = response.results
                            val movieList = movieResultList.map {
                                SearchMovieItem(it) { movie ->
                                    openMovieDetails(
                                        movie
                                    )
                                }
                            }.toList()
                            adapter.apply { addAll(movieList) }
                        }
                    },
                    {
                        // в случае ошибки
                        error -> Timber.e(error, "Ошибка при поиске фильмов")
                    }
                )
            compositeDisposable.add(disposableSearchedMovies)
        }
    }

    private fun openMovieDetails(movie: Movie) {
        val bundle = Bundle()
        bundle.putInt(KEY_MOVIE_ID, movie.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    override fun onStop() {
        super.onStop()

        adapter.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear() // диспозабл освободить!
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_MOVIE_ID = "movie_id"
        const val KEY_SEARCH = "search"
        const val SEARCH_DELAY_MILLISEC = 500L
    }
}
