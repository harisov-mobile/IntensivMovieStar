package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.network.MovieApiInterface
import ru.androidschool.intensiv.domain.repository.MovieRepositoryInternet
import javax.inject.Inject

class MovieRepositoryRemote @Inject constructor(private val api: MovieApiInterface) : MovieRepositoryInternet {
    override fun getMovies(): Single<List<Movie>> {
        return api.getNowPlayingMovies().map { it.results }
    }

    override fun getMovieDetails(movieId: Int): Single<MovieDetails> {
        return api.getMovieDetails(movieId)
    }

    override fun getMovieCredits(movieId: Int): Single<List<Actor>> {
        return api.getMovieCredits(movieId).map { it.cast }
    }
}