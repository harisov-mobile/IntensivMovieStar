package ru.androidschool.intensiv.ui.tvshows

import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.TvShow
import ru.androidschool.intensiv.ui.loadImage

class TvShowItem(
    private val content: TvShow,
    private val onClick: (tvShow: TvShow) -> Unit
) : Item() {
    override fun getLayout() = R.layout.item_tv_show

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.name
        viewHolder.tv_show_rating.rating = content.rating
        viewHolder.item_layout.setOnClickListener {
            onClick.invoke(content)
        }

        // TODO Получать из модели
        viewHolder.image_preview.loadImage(content.getPosterPathWithImageUrl())
    }
}
