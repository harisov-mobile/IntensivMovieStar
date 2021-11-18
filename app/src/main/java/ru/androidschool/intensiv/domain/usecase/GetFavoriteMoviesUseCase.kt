package ru.androidschool.intensiv.domain.usecase

import android.content.Context
import io.reactivex.Single
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.domain.repository.MovieRepository
import ru.androidschool.intensiv.utils.ViewFeature

class GetFavoriteMoviesUseCase(private val movieRepository: MovieRepository) {

    fun getMovies(context: Context): Single<List<MovieAndGenreAndActorAndProductionCompany>> {
        return movieRepository.getMovies(context, ViewFeature.FAVORITE)
    }
}
