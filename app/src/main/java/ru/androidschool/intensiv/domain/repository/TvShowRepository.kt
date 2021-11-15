package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.TvShowResponse

interface TvShowRepository {
    fun getPopularTvShows(): Single<TvShowResponse>
}
