package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.ActorDBO
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.mappers.ViewObjectMapper as ViewObjectMapper1

object ActorMapper : ViewObjectMapper1<ActorDBO, Actor> {
    override fun toViewObject(actor: Actor): ActorDBO {
        return ActorDBO(
            actorId = actor.id,
            name = actor.name,
            profilePath = actor.profilePath
        )
    }

}
