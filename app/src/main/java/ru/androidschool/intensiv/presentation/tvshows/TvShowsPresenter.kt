package ru.androidschool.intensiv.presentation.tvshows

import android.annotation.SuppressLint
import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.domain.usecase.GetPopularTvShowsUseCase
import ru.androidschool.intensiv.presentation.base.BasePresenter

class TvShowsPresenter(private val useCase: GetPopularTvShowsUseCase) : BasePresenter<TvShowsPresenter.TvShowsView>() {
    @SuppressLint("CheckResult")
    suspend fun getTvShows() {
        val tvShows = useCase.getPopularTvShows()
        view?.showTvShows(tvShows)
    }

    interface TvShowsView {
        fun showTvShows(tvShows: List<TvShow>)
        fun showLoading()
        fun hideLoading()
        fun showEmptyTvShows()
        fun showError()
    }
}
