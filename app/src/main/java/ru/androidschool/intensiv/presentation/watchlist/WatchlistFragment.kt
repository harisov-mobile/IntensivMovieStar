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
import ru.androidschool.intensiv.data.repository.FavoriteMovieRepositoryLocal

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

        // 1. Создаем фабрику
        val appContext = context?.applicationContext ?: throw IllegalStateException("App context not found")

        val watchlistViewModelFactory = WatchlistViewModelFactory(findNavController(), appContext, FavoriteMovieRepositoryLocal)

        // 2. Создаем ViewModel
        watchlistViewModel = ViewModelProvider(this, watchlistViewModelFactory).get(WatchListViewModel::class.java)

        // 3. Подписываемся
        watchlistViewModel.moviesLiveData.observe(viewLifecycleOwner,
        Observer { list ->
            adapter.clear()
            movies_recycler_view.adapter = adapter.apply { addAll(list) }
        })

        watchlistViewModel.fetchFavoriteMovies()
    }

    companion object {

        @JvmStatic
        fun newInstance() = WatchlistFragment()
    }
}
