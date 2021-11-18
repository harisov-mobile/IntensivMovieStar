package ru.androidschool.intensiv.presentation.watchlist

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_with_text.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dbo.MovieDBO
import ru.androidschool.intensiv.ui.loadImage

class MoviePreviewItem(
    private val content: MovieDBO,
    private val onClick: (movieDBO: MovieDBO) -> Unit
) : Item() {

    override fun getLayout() = R.layout.item_small

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.image_preview.setOnClickListener {
            onClick.invoke(content)
        }
        // TODO Получать из модели
        viewHolder.image_preview.loadImage(content.getPosterPathWithImageUrl())
    }
}
