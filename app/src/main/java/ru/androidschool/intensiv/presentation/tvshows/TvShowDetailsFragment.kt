package ru.androidschool.intensiv.presentation.tvshows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.database.MovieDatabase
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.TvShowDetails
import ru.androidschool.intensiv.data.mappers.TvShowMapper
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.presentation.applySchedulers
import ru.androidschool.intensiv.presentation.feed.ActorItem
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.loadImage
import ru.androidschool.intensiv.utils.Const
import timber.log.Timber

class TvShowDetailsFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var studioTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var imagePreview: ShapeableImageView
    private lateinit var movieRating: AppCompatRatingBar
    private lateinit var actorListRecyclerView: RecyclerView

    private lateinit var likeCheckBox: CheckBox
    private var tvShowDetails: TvShowDetails? = null

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // ?????? ?????? ???????? ???????????????????????? ???????????????????? ???? "????????????????????", ?? ???????????? ????-?????????????? ?????????? findViewById

        val view = inflater.inflate(R.layout.tv_show_details_fragment, container, false)

        titleTextView = view.findViewById(R.id.title_text_view)
        overviewTextView = view.findViewById(R.id.overview_text_view)
        studioTextView = view.findViewById(R.id.studio_text_view)
        genreTextView = view.findViewById(R.id.genre_text_view)
        releaseDateTextView = view.findViewById(R.id.release_date_text_view)
        imagePreview = view.findViewById(R.id.image_preview)
        movieRating = view.findViewById(R.id.movie_rating)
        actorListRecyclerView = view.findViewById(R.id.actor_list_recycler_view)

        likeCheckBox = view.findViewById(R.id.like_check_box)

        likeCheckBox.setOnCheckedChangeListener {
            _, isChecked ->
            onLikeCheckBoxChanged(isChecked)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShowId = requireArguments().getInt(Const.KEY_ID)

        adapter.clear()
        actorListRecyclerView.adapter = adapter

        compositeDisposable = CompositeDisposable()

        // ???????????????? ?????????????????? ???????????????????? ?? ??????????????????????
        val singleTvShowDetails = MovieApiClient.apiClient.getTvShowDetails(tvShowId)
        val disposableTvShowDetails = singleTvShowDetails
            .applySchedulers()
            .subscribe(
                {
                    tvShowDet ->
                    tvShowDetails = tvShowDet
                    tvShowDet?.let { it ->
                        titleTextView.text = it.name
                        overviewTextView.text = it.overview

                        studioTextView.text = it.productionCompanies.map {
                                company -> company.name }.joinToString()

                        genreTextView.text = it.genres.map {
                                it.name }.joinToString()

                        if (it.firstAirDate.length >= Const.YEAR_LENGTH) {
                            releaseDateTextView.text = it.firstAirDate.substring(0, Const.YEAR_LENGTH)
                        }

                        movieRating.rating = it.rating

                        imagePreview.loadImage(it.getPosterPathWithImageUrl())
                    }
                },
                {
                    error -> Timber.e(error, "???????????? ?????? ?????????????????? TvShowDetails")
                }
            )

        compositeDisposable.add(disposableTvShowDetails)

        // ???????????????? ???????????? ?????????????? ???? ?????????????????????? ?? "????????????????????????????" ?????? Groupie
        val singleTvShowCredits = MovieApiClient.apiClient.getTvShowCredits(tvShowId)
        val disposableTvShowCredits = singleTvShowCredits
            .applySchedulers()
            .subscribe(
                { // ?? ???????????? ?????????????????? ?????????????????? ????????????:
                    tvShowCreditsResponse ->
                    tvShowCreditsResponse?.let { tvShowCreditsResponse ->
                    val actorItemList = tvShowCreditsResponse.cast.map {
                            ActorItem(it) { actor -> openActorDetails(actor) } // ???????? ?????????????????????? ?????????????? ???????????????? ?? ?????????????????? ????????????
                        }.toList()
                        adapter.apply { addAll(actorItemList) }
                    }
                },
                {
                    error -> Timber.e(error, "???????????? ?????? ?????????????????? ???????????? ??????????????")
                }
            )

        compositeDisposable.add(disposableTvShowCredits)

        // ?????????????? ???? ????
        setTvShowLiked(tvShowId)
    }

    private fun openActorDetails(actor: Actor) {
//        val bundle = Bundle()
//        bundle.putString(KEY_ACTOR_ID, actor.id)
//        findNavController().navigate(R.id.actor_details_fragment, bundle, options)
        Toast.makeText(context, actor.name, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun onLikeCheckBoxChanged(isChecked: Boolean) {
        tvShowDetails?.let {
            val movieDao = MovieDatabase.get(requireContext()).movieDao()
            val tvShowDBO = TvShowMapper.toTvShowDBO(it)
            if (isChecked) {
                compositeDisposable.add(movieDao.insert(tvShowDBO)
                    .applySchedulers()
                    .subscribe({
                        Toast.makeText(context, "Written as liked", Toast.LENGTH_SHORT).show()
                    },
                        {
                            Toast.makeText(context, "Can not write as liked", Toast.LENGTH_SHORT).show()
                        }
                    )
                )
            } else {
                compositeDisposable.add(movieDao.delete(tvShowDBO)
                    .applySchedulers()
                    .subscribe({
                        Toast.makeText(context, "Removed from liked", Toast.LENGTH_SHORT).show()
                    },
                        {
                            Toast.makeText(context, "Can not remove from liked", Toast.LENGTH_SHORT).show()
                        }
                    )
                )
            }
        }
    }

    private fun setTvShowLiked(tvShowId: Int) {

        val movieDao = MovieDatabase.get(requireContext()).movieDao()

        compositeDisposable.add(movieDao.getTvShow(tvShowId)
            .applySchedulers()
            .subscribe({ tvShowDBO ->
                likeCheckBox.isChecked = tvShowDBO?.let { true } ?: let { false }
            },
                {
                    error -> Timber.e(error, "???????????? ?????? ?????????????????? ???????????????????????????? ??????????????????????.")
                }
            )
        )
    }

    companion object {
        const val KEY_ACTOR_ID = "actor_id"
    }
}
