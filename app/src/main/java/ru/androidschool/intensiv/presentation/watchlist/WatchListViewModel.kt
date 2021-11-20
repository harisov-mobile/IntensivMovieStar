package ru.androidschool.intensiv.presentation.watchlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.domain.repository.MovieRepositoryDB
import ru.androidschool.intensiv.domain.usecase.GetFavoriteMoviesUseCase
import ru.androidschool.intensiv.ui.applySchedulers
import timber.log.Timber

class WatchListViewModel(private val repository: MovieRepositoryDB) : ViewModel() {

    private var compositeDisposable: CompositeDisposable

    private val _moviesLiveData = MutableLiveData<List<MovieAndGenreAndActorAndProductionCompany>>()

    val moviesLiveData: LiveData<List<MovieAndGenreAndActorAndProductionCompany>>
        get() = _moviesLiveData

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun fetchFavoriteMovies() {

        compositeDisposable.add(
            GetFavoriteMoviesUseCase(repository).getMovies()
                .applySchedulers()
                .subscribe({ movies: List<MovieAndGenreAndActorAndProductionCompany> ->
                    _moviesLiveData.value = movies
                }, {
                    error -> Timber.e(error, "Ошибка при получении понравившегося списка фильмов")
                })
        )
    }
}
