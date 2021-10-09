package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.ui.feed.MainCardContainer
import ru.androidschool.intensiv.ui.feed.MovieItem
import ru.androidschool.intensiv.ui.feed.TvShowItem

import kotlinx.android.synthetic.main.tv_shows_fragment.*

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class TvShowsFragment : Fragment(R.layout.tv_shows_fragment) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.tv_shows_fragment, container, false)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Используя Мок-репозиторий получаем фэйковый список тв-шоу
        val tvShowList = MockRepository.getTvShows().map {
                    TvShowItem(it) { tvShow ->
                        openTvShowDetails(
                            tvShow
                        )
                    }
                }.toList()

        tvshows_recycler_view.adapter = adapter.apply { addAll(tvShowList) }
    }

    private fun openTvShowDetails(tvShow: TvShow) {
        val bundle = Bundle()
        bundle.putString(TvShowsFragment.KEY_TITLE, tvShow.title)
        findNavController().navigate(R.id.movie_details_fragment, bundle, null)
    }

    companion object {

        const val KEY_TITLE = "title"

//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            TvShowsFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}
