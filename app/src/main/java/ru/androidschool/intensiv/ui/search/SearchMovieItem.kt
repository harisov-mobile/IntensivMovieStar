package ru.androidschool.intensiv.ui.search

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Movie
import ru.androidschool.intensiv.data.TvShow
import ru.androidschool.intensiv.ui.loadImage

class SearchMovieItem(
    private val content: Movie,
    private val onClick: (movie: Movie) -> Unit
) : Item() {
    override fun getLayout() = R.layout.item_movie

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.tv_show_rating.rating = content.rating
        viewHolder.item_layout.setOnClickListener {
            onClick.invoke(content)
        }

        // TODO Получать из модели
        viewHolder.image_preview.loadImage(content.posterPath)
    }
}
