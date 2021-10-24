package ru.androidschool.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.MovieCreditsResponse
import ru.androidschool.intensiv.data.MovieDetails
import ru.androidschool.intensiv.data.MovieResponse
import ru.androidschool.intensiv.data.TvShowResponse

interface MovieApiInterface {

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("api_key") apiKey: String, @Query("language") language: String
    ): Call<MovieResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("api_key") apiKey: String, @Query("language") language: String
    ): Call<MovieResponse>

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String, @Query("language") language: String
    ): Call<MovieResponse>

    @GET("tv/popular")
    fun getPopularTvShows(@Query("api_key") apiKey: String, @Query("language") language: String
    ): Call<TvShowResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Path("movie_id") movieId: Int
    ): Call<MovieDetails>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Path("movie_id") movieId: Int
    ): Call<MovieCreditsResponse>


}
