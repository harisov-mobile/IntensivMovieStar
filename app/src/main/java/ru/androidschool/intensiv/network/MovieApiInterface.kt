package ru.androidschool.intensiv.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.*

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<MovieResponse>

    @GET("tv/popular")
    fun getPopularTvShows(
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<TvShowResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<MovieDetails>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<MovieCreditsResponse>

    @GET("tv/{tv_id}")
    fun getTvShowDetails(
        @Path("tv_id") tvShowId: Int,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<TvShowDetails>

    @GET("tv/{tv_id}/credits")
    fun getTvShowCredits(
        @Path("tv_id") tvShowId: Int,
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE
    ): Single<MovieCreditsResponse>

    @GET("search/movie")
    fun getSearchedMovies(
        @Query("api_key") apiKey: String = BuildConfig.THE_MOVIE_DATABASE_API,
        @Query("language") language: String = REQUIRED_LANGUAGE,
        @Query("query") query: String
    ): Single<SearchResponse>

    companion object {
        val REQUIRED_LANGUAGE = "ru-RU"
    }
}
