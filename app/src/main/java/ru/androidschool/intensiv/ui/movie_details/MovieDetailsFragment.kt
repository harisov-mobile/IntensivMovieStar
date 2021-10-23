package ru.androidschool.intensiv.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor
import ru.androidschool.intensiv.data.MockRepository
import ru.androidschool.intensiv.ui.feed.ActorItem
import ru.androidschool.intensiv.ui.feed.FeedFragment
import java.util.*

class MovieDetailsFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var studioTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var imagePreview: ImageView
    private lateinit var movieRating: AppCompatRatingBar
    private lateinit var actorListRecyclerView: RecyclerView

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

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

        val movieTitle = requireArguments().getString(FeedFragment.KEY_TITLE)

        val movie = MockRepository.getMovies().find { it.title == movieTitle }

        movie?.let {
            titleTextView.text = movie.title
            overviewTextView.text = movie.overview
            studioTextView.text = movie.studio
            movieRating.rating = movie.rating

            val cal = Calendar.getInstance()
            cal.time = movie.releaseDate

            releaseDateTextView.text = cal.get(Calendar.YEAR).toString()

            val genreListSize = movie.genreList.size
            var genreString = ""
            for (i in 0..(genreListSize - 1)) {
                genreString = genreString + ", " + movie.genreList[i].name // хитрый ход - чтобы в конце не обрезать, сделаю запятую с пробелом вначале, а потом с начала и удалю
            }
            genreString = genreString.substringAfter(' ')

            genreTextView.text = genreString

            // получаем список актеров из фильма и "приготавливаем" для Groupie
            val actorItemList = movie.actorList.map {
                ActorItem(it) { actor -> openActorDetails(actor) } // если понадобится открыть фрагмент с описанием актера
            }.toList()

            actorListRecyclerView.adapter = adapter.apply { addAll(actorItemList) }

            Picasso.get()
                .load(movie.previewUrl)
                .into(imagePreview)
        }
    }

    private fun openActorDetails(actor: Actor) {
//        val bundle = Bundle()
//        bundle.putString(KEY_ACTOR_ID, actor.id)
//        findNavController().navigate(R.id.actor_details_fragment, bundle, options)
        Toast.makeText(context, "" + actor.name + " " + actor.surname, Toast.LENGTH_SHORT).show()
    }

    companion object {

        const val KEY_ACTOR_ID = "actor_id"
    }
}
