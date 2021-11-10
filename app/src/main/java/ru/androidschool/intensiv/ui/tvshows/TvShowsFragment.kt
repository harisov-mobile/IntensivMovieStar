package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.applyProgressBar
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.utils.Const
import timber.log.Timber

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

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

        tvshows_recycler_view.adapter = adapter

        compositeDisposable = CompositeDisposable()

        // Получаем список сериалов
        val singlePopularTvShows = MovieApiClient.apiClient.getPopularTvShows()
        val disposablePopularTvShows = singlePopularTvShows
            .applySchedulers()
            .applyProgressBar(progress_bar)
            .subscribe(
                { // в случае успешного получения данных:
                    response ->
                    response?.let { response ->
                    val tvShowResultList = response.results
                    val tvShowList = tvShowResultList.map {
                        TvShowItem(it) { tvShow ->
                            openTvShowDetails(
                                tvShow
                            )
                        }
                    }.toList()
                    adapter.apply { addAll(tvShowList) }
                    }
                },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении телесериалов")
                }
            )

        compositeDisposable.add(disposablePopularTvShows)
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
        compositeDisposable.clear() // диспозабл освободить!
    }
}
