package ru.androidschool.intensiv.presentation.watchlist

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.data.dbo.MovieDBO
import ru.androidschool.intensiv.domain.repository.MovieRepository
import ru.androidschool.intensiv.domain.usecase.GetFavoriteMoviesUseCase
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.utils.Const
import timber.log.Timber

class WatchListViewModel(private val navController: NavController, private val appContext: Context, private val repository: MovieRepository) : ViewModel() {

    // у меня все работает - список понравившихся фильмов показывает,
    // но что-то я сомневаюсь, что так можно - понапихать в конструктор ViewModel всяких нужных мне
    // реквизитов типа navController, appContext - и потом их использовать.

    // А как сделать по-другому, чтобы и список в LiveData формировался, и при нажатии на элемент списка
    // чтобы отрабатывала процедура openMovieDetails ? - я не догадался.

    private var compositeDisposable: CompositeDisposable
    private val _moviesLiveData = MutableLiveData<List<MoviePreviewItem>>()

    val moviesLiveData: LiveData<List<MoviePreviewItem>>
        get() = _moviesLiveData

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun fetchFavoriteMovies() {

        compositeDisposable.add(
            GetFavoriteMoviesUseCase(repository).getMovies(appContext)
                .applySchedulers()
                .subscribe({ movies: List<MovieAndGenreAndActorAndProductionCompany> ->
                    val movieItemList = movies.map {
                        val movie = it.movieDBO
                        MoviePreviewItem(movie) { movie ->
                            openMovieDetails(movie)
                        }
                    }
                    _moviesLiveData.value = movieItemList
                }, {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении понравившегося списка фильмов")
                })
        )
    }

    private fun openMovieDetails(movie: MovieDBO) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, movie.movieId)
        navController.navigate(R.id.movie_details_fragment, bundle)
    }
}
