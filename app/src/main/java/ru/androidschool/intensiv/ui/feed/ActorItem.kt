package ru.androidschool.intensiv.ui.feed

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_actor.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor

class ActorItem(
    private val content: Actor,
    private val onClick: (actor: Actor) -> Unit
) : Item() {

    // Ячейка для актера также должна быть наследником Item
    // ячейка должна реализовать 2 метода:
    override fun getLayout() = R.layout.item_actor

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_name_text_view.text = content.name
        viewHolder.actor_surname_text_view.text = content.surname
        viewHolder.item_layout.setOnClickListener {
            onClick.invoke(content)
        }

        // TODO Получать из модели
        Picasso.get()
            .load("https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhYjQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg")
            .into(viewHolder.image_preview)
    }
}
