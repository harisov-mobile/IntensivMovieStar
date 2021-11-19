package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.utils.ViewFeature

interface MovieRepositoryInternet {
    fun getMovies(): Single<List<Movie>>

    fun getMovieDetails(movieId: Int): Single<MovieDetails>
}