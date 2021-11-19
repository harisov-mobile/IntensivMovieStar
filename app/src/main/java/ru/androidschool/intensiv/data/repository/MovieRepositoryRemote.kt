package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.data.network.MovieApiInterface
import ru.androidschool.intensiv.domain.repository.MovieRepository
import ru.androidschool.intensiv.utils.ViewFeature
import javax.inject.Inject

class MovieRepositoryRemote @Inject constructor(private val api: MovieApiInterface) : MovieRepository {
    override fun getMovies(viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>> {
        TODO()
    }
}