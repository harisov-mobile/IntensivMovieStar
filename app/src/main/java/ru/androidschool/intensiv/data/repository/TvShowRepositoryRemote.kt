package ru.androidschool.intensiv.data.repository

import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.domain.repository.TvShowRepository

object TvShowRepositoryRemote : TvShowRepository {
    override suspend fun getPopularTvShows(): List<TvShow> {
        return MovieApiClient.apiClient.getPopularTvShows().results
    }
}
