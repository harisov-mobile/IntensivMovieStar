package ru.androidschool.intensiv.presentation.feed

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_actor.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.dto.Actor

class ActorItem(
    private val content: Actor,
    private val onClick: (actor: Actor) -> Unit
) : Item() {

    // Ячейка для актера также должна быть наследником Item
    // ячейка должна реализовать 2 метода:
    override fun getLayout() = R.layout.item_actor

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val substr = content.name.split(' ', limit = 2)
        val substrSize = substr.size

        viewHolder.actor_name_text_view.text = if (substrSize > 0) substr[0] else ""
        viewHolder.actor_surname_text_view.text = if (substrSize > 1) substr[1] else ""
        viewHolder.item_layout.setOnClickListener {
            onClick.invoke(content)
        }

        // TODO Получать из модели
        Picasso.get()
            .load(content.getProfilePathWithImageUrl())
            .placeholder(R.drawable.ic_person_placeholder)
            .into(viewHolder.image_preview)
    }
}
