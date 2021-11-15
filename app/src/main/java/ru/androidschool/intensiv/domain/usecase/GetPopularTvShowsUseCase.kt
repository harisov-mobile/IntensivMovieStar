package ru.androidschool.intensiv.domain.usecase

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.TvShowResponse
import ru.androidschool.intensiv.domain.repository.TvShowRepository

class GetPopularTvShowsUseCase(private val tvShowRepository: TvShowRepository) {
    fun getPopularTvShows(): Single<TvShowResponse> {
        return tvShowRepository.getPopularTvShows()
    }
}
