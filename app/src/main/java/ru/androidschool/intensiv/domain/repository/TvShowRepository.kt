package ru.androidschool.intensiv.domain.repository

import ru.androidschool.intensiv.data.dto.TvShowResponse

interface TvShowRepository {
    fun getPopularTvShows(): TvShowResponse
}
