package ru.androidschool.intensiv.presentation.tvshows

import android.annotation.SuppressLint
import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.domain.usecase.GetPopularTvShowsUseCase
import ru.androidschool.intensiv.presentation.base.BasePresenter
import timber.log.Timber

class TvShowsPresenter(private val useCase: GetPopularTvShowsUseCase) : BasePresenter<TvShowsPresenter.TvShowsView>()
{   @SuppressLint("CheckResult")
    fun getTvShows() {
        useCase.getPopularTvShows()
            .subscribe(
                {
                    // в случае успешного получения данных:
                    response ->
                    response?.let { response ->
                    val tvShowResultList = response.results
                    view?.showTvShows(tvShowResultList)
                    }
                },
                { t ->
                    Timber.e(t, t.toString())
                    view?.showEmptyTvShows()
                })
    }

    interface TvShowsView {
        fun showTvShows(tvShows: List<TvShow>)
        fun showLoading()
        fun hideLoading()
        fun showEmptyTvShows()
        fun showError()
    }
}
