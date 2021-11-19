package ru.androidschool.intensiv.presentation.movie_details

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
import ru.androidschool.intensiv.data.dbo.*
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.mappers.ActorMapper
import ru.androidschool.intensiv.data.mappers.GenreMapper
import ru.androidschool.intensiv.data.mappers.MovieMapper
import ru.androidschool.intensiv.data.mappers.ProductionCompanyMapper
import ru.androidschool.intensiv.data.repository.MovieRepositoryLocal
import ru.androidschool.intensiv.data.repository.MovieRepositoryRemote
import ru.androidschool.intensiv.presentation.applySchedulers
import ru.androidschool.intensiv.presentation.feed.ActorItem
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.loadImage
import ru.androidschool.intensiv.utils.Const
import ru.androidschool.intensiv.utils.ViewFeature
import timber.log.Timber
import javax.inject.Inject

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

    private lateinit var movieRepositoryLocal: MovieRepositoryLocal

    @Inject
    lateinit var movieRepositoryRemote: MovieRepositoryRemote

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

        // учебный комментарий - инъекция зависимости Даггером (//.movieDetailsModule(MovieDetailsModule()) - почему-то не запускалось без этой строки)
        DaggerMovieDetailsComponent.builder()
            .build().inject(this)

        movieRepositoryLocal = MovieRepositoryLocal.get()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getInt(Const.KEY_ID)

        actorListRecyclerView.adapter = adapter

        compositeDisposable = CompositeDisposable()

        // Получаем детальную информацию о фильме
        // учебный комментарий - movieRepositoryRemote должна "прилететь" через Даггер
        val singleMovieDetails = movieRepositoryRemote.getMovieDetails(movieId)
        val disposableMovieDetails = singleMovieDetails
            .applySchedulers()
            .subscribe(
                { movieDet ->
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
                    error -> Timber.e(error, "Ошибка при получении детальной информации о фильме")
                }
            )

        compositeDisposable.add(disposableMovieDetails)

        // получаем список актеров из фильма и "приготавливаем" для Groupie
        val singleMovieCredits = movieRepositoryRemote.getMovieCredits(movieId)
        val disposableMovieCredits = singleMovieCredits
            .applySchedulers()
            .subscribe(
                { it ->
                    actorList = it // сохраню для последующей записи в БД
                    val actorItemList = it.map {
                        ActorItem(it) { actor -> openActorDetails(actor) } // если понадобится открыть фрагмент с описанием актера
                    }.toList()
                    adapter.apply { addAll(actorItemList) }
                },
                {
                    error -> Timber.e(error, "Ошибка при получении списка актеров")
                }
            )

        compositeDisposable.add(disposableMovieCredits)

        setMovieLiked(movieId)
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
        compositeDisposable.clear()
    }

    private fun onLikeCheckBoxChanged(isChecked: Boolean) {
        movieDetails?.let {

            if (isChecked) {
                saveLikedMovieToDB(it, actorList ?: emptyList())
            } else {
                val movieDBO = MovieMapper.toMovieDBO(it, ViewFeature.FAVORITE)
                compositeDisposable.add(movieRepositoryLocal.delete(movieDBO)
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

    private fun saveLikedMovieToDB(movieDet: MovieDetails, actors: List<Actor>) {
        val movieDBO = MovieMapper.toMovieDBO(movieDet, ViewFeature.FAVORITE)

        val genreDBOList: List<GenreDBO> = GenreMapper.toViewObject(movieDet.genres)
        val actorDBOList: List<ActorDBO> = ActorMapper.toViewObject(actors)
        val productionCompanyDBOList: List<ProductionCompanyDBO> =
            ProductionCompanyMapper.toViewObject(movieDet.productionCompanies)

        val movieAndGenreCrossRefList: List<MovieAndGenreCrossRef> =
            MovieMapper.toMovieAndGenreCrossRefList(movieDBO.movieId, genreDBOList)

        val movieAndActorCrossRefList: List<MovieAndActorCrossRef> =
            MovieMapper.toMovieAndActorCrossRefList(movieDBO.movieId, actorDBOList)

        val movieAndProductionCompanyCrossRefList: List<MovieAndProductionCompanyCrossRef> =
            MovieMapper.toMovieAndProductionCompanyCrossRefList(movieDBO.movieId, productionCompanyDBOList)

        compositeDisposable.add(movieRepositoryLocal.insert(movieDBO)
            .andThen(movieRepositoryLocal.insertGenres(genreDBOList))
            .andThen(movieRepositoryLocal.insertActors(actorDBOList))
            .andThen(movieRepositoryLocal.insertProductionCompanies(productionCompanyDBOList))
            .andThen(movieRepositoryLocal.insertGenreJoins(movieAndGenreCrossRefList))
            .andThen(movieRepositoryLocal.insertActorJoins(movieAndActorCrossRefList))
            .andThen(movieRepositoryLocal.insertProductionCompanyJoins(movieAndProductionCompanyCrossRefList))
            .applySchedulers()
            .subscribe({
                Toast.makeText(context, "Written this movie as liked", Toast.LENGTH_SHORT).show()
            },
                {
                    Toast.makeText(context, "Can not write this movie as liked", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }

    private fun setMovieLiked(movieId: Int) {

        compositeDisposable.add(movieRepositoryLocal.getFavoriteMovie(movieId, ViewFeature.FAVORITE)
            .applySchedulers()
            .subscribe({ movieDBO ->
                    likeCheckBox.isChecked = movieDBO?.let { true } ?: let { false }
                },
                {
                    error -> Timber.e(error, "Ошибка при получении понравившегося фильма.")
                }
            )
        )
    }

    companion object {

        const val KEY_ACTOR_ID = "actor_id"
        private const val TAG = "MovieDetailsFragment"
    }
}
