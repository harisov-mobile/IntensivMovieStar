package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieDetails

interface MovieRepositoryInternet {
    fun getMovies(): Single<List<Movie>>

    fun getMovieDetails(movieId: Int): Single<MovieDetails>

    fun getMovieCredits(movieId: Int): Single<List<Actor>>
}
