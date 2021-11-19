package ru.androidschool.intensiv.domain.usecase

import io.reactivex.Single
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.domain.repository.MovieRepositoryDB
import ru.androidschool.intensiv.utils.ViewFeature

class GetFavoriteMoviesUseCase(private val movieRepository: MovieRepositoryDB) {

    fun getMovies(): Single<List<MovieAndGenreAndActorAndProductionCompany>> {
        return movieRepository.getMovies(ViewFeature.FAVORITE)
    }
}
