package ru.androidschool.intensiv.ui.tvshows

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor
import ru.androidschool.intensiv.data.MovieCreditsResponse
import ru.androidschool.intensiv.data.TvShowDetails
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.feed.ActorItem
import ru.androidschool.intensiv.ui.loadImage

class TvShowDetailsFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var studioTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var imagePreview: ShapeableImageView
    private lateinit var movieRating: AppCompatRatingBar
    private lateinit var actorListRecyclerView: RecyclerView

    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // так как Гугл рекомендовал отказаться от "синтетиков", я сделал по-старому через findViewById

        val view = inflater.inflate(R.layout.tv_show_details_fragment, container, false)

        titleTextView = view.findViewById(R.id.title_text_view)
        overviewTextView = view.findViewById(R.id.overview_text_view)
        studioTextView = view.findViewById(R.id.studio_text_view)
        genreTextView = view.findViewById(R.id.genre_text_view)
        releaseDateTextView = view.findViewById(R.id.release_date_text_view)
        imagePreview = view.findViewById(R.id.image_preview)
        movieRating = view.findViewById(R.id.movie_rating)
        actorListRecyclerView = view.findViewById(R.id.actor_list_recycler_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShowId = requireArguments().getInt(TvShowsFragment.KEY_TV_SHOW_ID)

        adapter = GroupAdapter<GroupieViewHolder>()
        actorListRecyclerView.adapter = adapter

        // Вызываем метод getMovieDetails()
        val callTvShowDetails = MovieApiClient.apiClient.getTvShowDetails(tvShowId, BuildConfig.THE_MOVIE_DATABASE_API, "ru")
        callTvShowDetails.enqueue(object : Callback<TvShowDetails> {
            override fun onResponse(
                call: Call<TvShowDetails>,
                response: Response<TvShowDetails>
            ) {
                // Получаем результат
                val tvShowDetails = response.body()!!

                titleTextView.text = tvShowDetails.name
                overviewTextView.text = tvShowDetails.overview

                studioTextView.text = tvShowDetails.productionCompanies.map {
                        company -> company.name }.joinToString(", ")

                genreTextView.text = tvShowDetails.genres.map {
                        genre -> genre.name }.joinToString(", ")

                releaseDateTextView.text = tvShowDetails.firstAirDate.substring(0, 4)

                movieRating.rating = tvShowDetails.rating

                imagePreview.loadImage(tvShowDetails.posterPath)
            }
            override fun onFailure(call: Call<TvShowDetails>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
            }
        })

        // получаем список актеров из фильма и "приготавливаем" для Groupie
        // Вызываем метод getMovieDetails()
        val callTvShowCredits = MovieApiClient.apiClient.getTvShowCredits(tvShowId, BuildConfig.THE_MOVIE_DATABASE_API, "ru")
        callTvShowCredits.enqueue(object : Callback<MovieCreditsResponse> {
            override fun onResponse(
                call: Call<MovieCreditsResponse>,
                response: Response<MovieCreditsResponse>
            ) {
                // Получаем результат
                val movieCreditsResponse = response.body()!!

                val actorItemList = movieCreditsResponse.cast.map {
                    ActorItem(it) { actor -> openActorDetails(actor) } // если понадобится открыть фрагмент с описанием актера
                }.toList()
                adapter.apply { addAll(actorItemList) }
            }
            override fun onFailure(call: Call<MovieCreditsResponse>, t: Throwable) {
                // Log error here since request failed
                Log.e(TAG, t.toString())
            }
        })
    }

    private fun openActorDetails(actor: Actor) {
//        val bundle = Bundle()
//        bundle.putString(KEY_ACTOR_ID, actor.id)
//        findNavController().navigate(R.id.actor_details_fragment, bundle, options)
        Toast.makeText(context, actor.name, Toast.LENGTH_SHORT).show()
    }

    companion object {

        const val KEY_ACTOR_ID = "actor_id"
        private const val TAG = "TvShowDetailsFragment"
    }
}
