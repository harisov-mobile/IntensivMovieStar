package ru.androidschool.intensiv.presentation.watchlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import ru.androidschool.intensiv.domain.repository.MovieRepository
import java.lang.IllegalArgumentException

class WatchlistViewModelFactory(private val navController: NavController, private val appContext: Context, private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchListViewModel::class.java)) {
            return WatchListViewModel(navController, appContext, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
