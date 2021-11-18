package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.TvShowResponse
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.domain.repository.TvShowRepository

object TvShowRepositoryRemote : TvShowRepository {
    override fun getPopularTvShows(): Single<TvShowResponse> {
        return MovieApiClient.apiClient.getPopularTvShows()
    }
}
