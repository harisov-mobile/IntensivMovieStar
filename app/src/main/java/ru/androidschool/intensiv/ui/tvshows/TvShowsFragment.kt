package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.data.TvShowResponse
import ru.androidschool.intensiv.network.MovieApiClient

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
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

        tvshows_recycler_view.adapter = adapter

        // Вызываем метод getPopularTvShows()
        val callPopularTvShows = MovieApiClient.apiClient.getPopularTvShows()
        callPopularTvShows.enqueue(object : Callback<TvShowResponse> {
            override fun onResponse(
                call: Call<TvShowResponse>,
                response: Response<TvShowResponse>
            ) {
                // Получаем результат
                response.body()?.let{ body ->
                    val tvShowResultList = body.results

                    val tvShowList = tvShowResultList.map {
                        TvShowItem(it) { tvShow ->
                            openTvShowDetails(
                                tvShow
                            )
                        }
                    }.toList()
                    adapter.apply { addAll(tvShowList) }
                }
            }

            override fun onFailure(call: Call<TvShowResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
            }
        })
    }

    private fun openTvShowDetails(tvShow: TvShow) {
        val bundle = Bundle()
        bundle.putInt(KEY_TV_SHOW_ID, tvShow.id)
        findNavController().navigate(R.id.tv_show_details_fragment, bundle, options)
    }

    override fun onStop() {
        super.onStop()

        // при клике на сериал и возврате в список
        // происходило добавление в адаптер, в результате чего задваивались списки сериалов
        // поэтому очищаю
        adapter.clear()
    }

    companion object {
        const val KEY_TV_SHOW_ID = "tv_show_id"
        private val TAG = "TvShowsFragment"
    }
}
