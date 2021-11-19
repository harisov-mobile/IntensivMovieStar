package ru.androidschool.intensiv.presentation.movie_details

import dagger.Module
import dagger.Provides
import ru.androidschool.intensiv.MovieFinderApp
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.network.MovieApiInterface
import ru.androidschool.intensiv.data.repository.MovieRepositoryRemote
import javax.inject.Singleton

@Module
class MovieDetailsModule() {

    @Singleton
    @Provides
    fun provideMovieApi(): MovieApiInterface {
        return MovieApiClient.apiClient
    }

    @Singleton
    @Provides
    fun provideMovieRepositoryRemote(api: MovieApiInterface): MovieRepositoryRemote {
        return MovieRepositoryRemote(api)
    }
}