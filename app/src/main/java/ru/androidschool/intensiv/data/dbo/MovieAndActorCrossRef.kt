package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "actorId"])
data class MovieAndActorCrossRef(
    val movieId: Int,
    val actorId: Int
)
