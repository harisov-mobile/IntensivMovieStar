package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.ActorDBO
import ru.androidschool.intensiv.data.dto.Actor

object ActorMapper {
    fun toActorDBOList(actors: List<Actor>): List<ActorDBO> {
        return actors.map { toActorDBO(it) }
    }

    fun toActorDBO(actor: Actor): ActorDBO {
        return ActorDBO(
            actorId = actor.id,
            name = actor.name,
            profilePath = actor.profilePath
        )
    }
}
