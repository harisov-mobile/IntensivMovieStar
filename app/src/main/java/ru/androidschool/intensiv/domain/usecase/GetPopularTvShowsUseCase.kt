package ru.androidschool.intensiv.domain.usecase

import ru.androidschool.intensiv.data.dto.TvShowResponse
import ru.androidschool.intensiv.domain.repository.TvShowRepository

class GetPopularTvShowsUseCase(private val tvShowRepository: TvShowRepository) {
    fun getPopularTvShows(): TvShowResponse {
        return tvShowRepository.getPopularTvShows()
    }
}
