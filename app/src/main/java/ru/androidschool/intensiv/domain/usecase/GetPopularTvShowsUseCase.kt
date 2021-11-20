package ru.androidschool.intensiv.domain.usecase

import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.domain.repository.TvShowRepository

class GetPopularTvShowsUseCase(private val tvShowRepository: TvShowRepository) {
    suspend fun getPopularTvShows(): List<TvShow> {
        return tvShowRepository.getPopularTvShows()
    }
}
