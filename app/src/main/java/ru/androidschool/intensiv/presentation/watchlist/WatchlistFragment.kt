package ru.androidschool.intensiv.presentation.watchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_watchlist.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dbo.MovieDBO
import ru.androidschool.intensiv.data.repository.MovieRepositoryLocal
import ru.androidschool.intensiv.utils.Const

class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var watchlistViewModel: WatchListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.clear()
        movies_recycler_view.layoutManager = GridLayoutManager(context, 4)
        movies_recycler_view.adapter = adapter

        compositeDisposable = CompositeDisposable()

        // 1. Создаем фабрику (учебный комментарий, в реальном проекте такого комментария не будет)
        val watchlistViewModelFactory = WatchlistViewModelFactory(MovieRepositoryLocal.get())

        // 2. Создаем ViewModel (учебный комментарий, в реальном проекте такого комментария не будет)
        watchlistViewModel = ViewModelProvider(this, watchlistViewModelFactory).get(WatchListViewModel::class.java)

        // 3. Подписываемся (учебный комментарий, в реальном проекте такого комментария не будет)
        watchlistViewModel.moviesLiveData.observe(viewLifecycleOwner,
            Observer { list ->
                adapter.clear()

                val movieItemList = list.map {
                    val movie = it.movieDBO
                    MoviePreviewItem(movie) { movie ->
                        openMovieDetails(movie)
                    }
                }
                movies_recycler_view.adapter = adapter.apply { addAll(movieItemList) }
            })

        // 4. Получаем список понравившихся фильмов (учебный комментарий, в реальном проекте такого комментария не будет)
        watchlistViewModel.fetchFavoriteMovies()
    }

    private fun openMovieDetails(movie: MovieDBO) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, movie.movieId)
        findNavController().navigate(R.id.movie_details_fragment, bundle)
    }

    companion object {
        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}
