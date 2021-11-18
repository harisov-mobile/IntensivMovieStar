package ru.androidschool.intensiv.presentation.tvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.data.repository.TvShowRepositoryImpl
import ru.androidschool.intensiv.domain.usecase.GetPopularTvShowsUseCase
import ru.androidschool.intensiv.utils.Const

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment), TvShowsPresenter.TvShowsView {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private val presenter: TvShowsPresenter by lazy {
        TvShowsPresenter(GetPopularTvShowsUseCase(TvShowRepositoryImpl))
    }

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Добавляем в presenter имплементацию TvShowsFragment
        presenter.attachView(this)

        presenter.getTvShows()
    }

    private fun openTvShowDetails(tvShow: TvShow) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, tvShow.id)
        findNavController().navigate(R.id.tv_show_details_fragment, bundle, options)
    }

    override fun onStop() {
        super.onStop()

        // при клике на сериал и возврате в список
        // происходило добавление в адаптер, в результате чего задваивались списки сериалов
        // поэтому очищаю
        adapter.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }

    override fun showTvShows(tvShows: List<TvShow>) {
        tvshows_recycler_view.adapter = adapter.apply {
            addAll(
                tvShows.map {
                        TvShowItem(it) { tvShow ->
                            openTvShowDetails(
                                tvShow
                            )
                        }
                    }.toList()
            )
        }
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
    }

    override fun showEmptyTvShows() {
        tvshows_recycler_view.adapter = adapter.apply { addAll(listOf()) }
    }

    override fun showError() {
        // не знаю, что делать если ошибка.
    }
}
