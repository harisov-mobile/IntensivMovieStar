package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.ActorDBO
import ru.androidschool.intensiv.data.dto.Actor

object ActorMapper : ViewObjectMapper<ActorDBO, Actor> {
    override fun toViewObject(actor: Actor): ActorDBO {
        return ActorDBO(
            actorId = actor.id,
            name = actor.name,
            profilePath = actor.profilePath
        )
    }
}
