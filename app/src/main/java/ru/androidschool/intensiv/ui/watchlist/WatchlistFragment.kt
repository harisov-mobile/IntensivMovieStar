package ru.androidschool.intensiv.ui.watchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.data.dbo.MovieDBO
import ru.androidschool.intensiv.database.MovieDatabase
import ru.androidschool.intensiv.utils.Const
import ru.androidschool.intensiv.utils.ViewFeature
import timber.log.Timber

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

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

        adapter.clear()
        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_recycler_view.adapter = adapter

        compositeDisposable = CompositeDisposable()

        getFavoriteMoviesFromDB()
    }

    private fun getFavoriteMoviesFromDB() {
        val movieDao = MovieDatabase.get(requireContext()).movieDao()

        compositeDisposable.add(movieDao.getMovies(ViewFeature.FAVORITE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ movies: List<MovieAndGenreAndActorAndProductionCompany> ->
                val movieItemList = movies.map {
                    val movie = it.movieDBO
                    MoviePreviewItem(movie) { movie ->
                        openMovieDetails(movie)
                    }
                }
                adapter.apply { addAll(movieItemList) }
            },
                {
                    // в случае ошибки
                        error -> Timber.e(error, "Ошибка при получении понравившегося телесериала.")
                }
            )
        )
    }

    private fun openMovieDetails(movie: MovieDBO) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, movie.movieId)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    companion object {

        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}
