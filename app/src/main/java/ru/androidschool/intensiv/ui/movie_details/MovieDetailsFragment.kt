package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
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
import io.reactivex.disposables.CompositeDisposable
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.addSchedulers
import ru.androidschool.intensiv.ui.feed.ActorItem
import ru.androidschool.intensiv.ui.feed.FeedFragment
import ru.androidschool.intensiv.ui.loadImage
import timber.log.Timber

class MovieDetailsFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var studioTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var imagePreview: ShapeableImageView
    private lateinit var movieRating: AppCompatRatingBar
    private lateinit var actorListRecyclerView: RecyclerView

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // так как Гугл рекомендовал отказаться от "синтетиков", я сделал по-старому через findViewById

        val view = inflater.inflate(R.layout.movie_details_fragment, container, false)

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

        val movieId = requireArguments().getInt(FeedFragment.KEY_MOVIE_ID)

        actorListRecyclerView.adapter = adapter

        compositeDisposable = CompositeDisposable()

        // Получаем детальную информацию о фильме
        val singleMovieDetails = MovieApiClient.apiClient.getMovieDetails(movieId)
        val disposableMovieDetails = singleMovieDetails
            .addSchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                    movieDetails ->
                    movieDetails?.let { movieDetails ->
                    titleTextView.text = movieDetails.title
                    overviewTextView.text = movieDetails.overview

                    studioTextView.text = movieDetails.productionCompanies.map {
                            company -> company.name }.joinToString()

                    genreTextView.text = movieDetails.genres.map {
                            genre -> genre.name }.joinToString()

                    releaseDateTextView.text = movieDetails.releaseDate.substring(0, 4)

                    movieRating.rating = movieDetails.rating

                    imagePreview.loadImage(movieDetails.posterPath)
                    }
                },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении NowPlayingMovies")
                }
            )

        compositeDisposable.add(disposableMovieDetails)

        // получаем список актеров из фильма и "приготавливаем" для Groupie
        val singleMovieCredits = MovieApiClient.apiClient.getMovieCredits(movieId)
        val disposableMovieCredits = singleMovieCredits
            .addSchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                    movieCreditsResponse ->
                    movieCreditsResponse?.let { movieCreditsResponse ->
                    val actorItemList = movieCreditsResponse.cast.map {
                        ActorItem(it) { actor -> openActorDetails(actor) } // если понадобится открыть фрагмент с описанием актера
                    }.toList()
                    adapter.apply { addAll(actorItemList) }
                    }
                },
                {
                    // в случае ошибки
                    error -> Timber.e(error, "Ошибка при получении списка актеров")
                }
            )

        compositeDisposable.add(disposableMovieCredits)
    }

    private fun openActorDetails(actor: Actor) {
//        val bundle = Bundle()
//        bundle.putString(KEY_ACTOR_ID, actor.id)
//        findNavController().navigate(R.id.actor_details_fragment, bundle, options)
        Toast.makeText(context, actor.name, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        adapter.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear() // диспозабл освободить!
    }

    companion object {

        const val KEY_ACTOR_ID = "actor_id"
        private const val TAG = "MovieDetailsFragment"
    }
}
