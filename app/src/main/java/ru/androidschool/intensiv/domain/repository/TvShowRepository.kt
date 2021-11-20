package ru.androidschool.intensiv.domain.repository

import ru.androidschool.intensiv.data.dto.TvShow

interface TvShowRepository {
    suspend fun getPopularTvShows(): List<TvShow>
}
