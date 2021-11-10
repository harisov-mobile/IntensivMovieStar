package ru.androidschool.intensiv.ui.movie_details

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.dbo.*
import ru.androidschool.intensiv.database.MovieDao
import ru.androidschool.intensiv.database.MovieDatabase
import ru.androidschool.intensiv.network.MovieApiClient
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.feed.ActorItem
import ru.androidschool.intensiv.ui.loadImage
import ru.androidschool.intensiv.utils.Const
import ru.androidschool.intensiv.utils.MovieFinderAppConverter
import ru.androidschool.intensiv.utils.ViewFeature
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

    private lateinit var likeCheckBox: CheckBox
    private var movieDetails: MovieDetails? = null
    private var actorList: List<Actor>? = null

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

        likeCheckBox = view.findViewById(R.id.like_check_box)

        likeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onLikeCheckBoxChanged(isChecked)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getInt(Const.KEY_ID)

        actorListRecyclerView.adapter = adapter

        compositeDisposable = CompositeDisposable()

        // Получаем детальную информацию о фильме
        val singleMovieDetails = MovieApiClient.apiClient.getMovieDetails(movieId)
        val disposableMovieDetails = singleMovieDetails
            .applySchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                        movieDet ->
                    movieDetails = movieDet // сохраню для последующей записи в БД
                    movieDet?.let { it ->
                        titleTextView.text = it.title
                        overviewTextView.text = it.overview

                        studioTextView.text = it.productionCompanies.map { company -> company.name }.joinToString()

                        genreTextView.text = it.genres.map { genre -> genre.name }.joinToString()

                        if (it.releaseDate.length >= Const.YEAR_LENGTH) {
                            releaseDateTextView.text = it.releaseDate.substring(0, Const.YEAR_LENGTH)
                        }

                        movieRating.rating = it.rating

                        imagePreview.loadImage(it.getPosterPathWithImageUrl())
                    }
                },
                {
                    // в случае ошибки
                        error ->
                    Timber.e(error, "Ошибка при получении детальной информации о фильме")
                }
            )

        compositeDisposable.add(disposableMovieDetails)

        // получаем список актеров из фильма и "приготавливаем" для Groupie
        val singleMovieCredits = MovieApiClient.apiClient.getMovieCredits(movieId)
        val disposableMovieCredits = singleMovieCredits
            .applySchedulers()
            .subscribe(
                { // в случае успешного получения данных:
                        movieCreditsResponse ->
                    movieCreditsResponse?.let { movieCreditsResponse ->
                        actorList = movieCreditsResponse.cast // сохраню для последующей записи в БД
                        val actorItemList = movieCreditsResponse.cast.map {
                            ActorItem(it) { actor -> openActorDetails(actor) } // если понадобится открыть фрагмент с описанием актера
                        }.toList()
                        adapter.apply { addAll(actorItemList) }
                    }
                },
                {
                    // в случае ошибки
                        error ->
                    Timber.e(error, "Ошибка при получении списка актеров")
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

    private fun onLikeCheckBoxChanged(isChecked: Boolean) {
        movieDetails?.let {
            val movieDao = MovieDatabase.get(requireContext()).movieDao()

            if (isChecked) {
                saveLikedMovieToDB(it, actorList ?: emptyList(), movieDao)
            } else {
                val movieDBO = MovieFinderAppConverter.toMovieDBO(it, ViewFeature.FAVORITE)
                compositeDisposable.add(movieDao.delete(movieDBO)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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

    private fun saveLikedMovieToDB(movieDet: MovieDetails, actors: List<Actor>, movieDao: MovieDao) {
        val movieDBO = MovieFinderAppConverter.toMovieDBO(movieDet, ViewFeature.FAVORITE)

        val genreDBOList: List<GenreDBO> = MovieFinderAppConverter.toGenreDBOList(movieDet.genres)
        val actorDBOList: List<ActorDBO> = MovieFinderAppConverter.toActorDBOList(actors)
        val productionCompanyDBOList: List<ProductionCompanyDBO> =
            MovieFinderAppConverter.toProductionCompanyDBOList(movieDet.productionCompanies)

        val movieAndGenreCrossRefList: List<MovieAndGenreCrossRef> =
            MovieFinderAppConverter.toMovieAndGenreCrossRefList(movieDBO.movieId, genreDBOList)

        val movieAndActorCrossRefList: List<MovieAndActorCrossRef> =
            MovieFinderAppConverter.toMovieAndActorCrossRefList(movieDBO.movieId, actorDBOList)

        val movieAndProductionCompanyCrossRefList: List<MovieAndProductionCompanyCrossRef> =
            MovieFinderAppConverter.toMovieAndProductionCompanyCrossRefList(movieDBO.movieId, productionCompanyDBOList)

        compositeDisposable.add(movieDao.insert(movieDBO)
            .andThen(movieDao.insertGenres(genreDBOList))
            .andThen(movieDao.insertActors(actorDBOList))
            .andThen(movieDao.insertProductionCompanies(productionCompanyDBOList))
            .andThen(movieDao.insertGenreJoins(movieAndGenreCrossRefList))
            .andThen(movieDao.insertActorJoins(movieAndActorCrossRefList))
            .andThen(movieDao.insertProductionCompanyJoins(movieAndProductionCompanyCrossRefList))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(context, "Written this movie as liked", Toast.LENGTH_SHORT).show()
            },
                {
                    Toast.makeText(context, "Can not write this movie as liked", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

    companion object {

        const val KEY_ACTOR_ID = "actor_id"
        private const val TAG = "MovieDetailsFragment"
    }
}
