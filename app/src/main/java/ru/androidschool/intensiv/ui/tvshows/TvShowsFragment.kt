package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.tv_shows_fragment.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.data.TvShow

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

        // Используя Мок-репозиторий получаем фэйковый список тв-шоу
        val tvShowList = MockRepository.getTvShows().map {
                    TvShowItem(it) { tvShow -> openTvShowDetails(tvShow) }
                }.toList()

        tvshows_recycler_view.adapter = adapter.apply { addAll(tvShowList) }
    }

    private fun openTvShowDetails(tvShow: TvShow) {
        val bundle = Bundle()
        bundle.putString(KEY_TITLE, tvShow.title)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    companion object {

        const val KEY_TITLE = "title"
    }
}
