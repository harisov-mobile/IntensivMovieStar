package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity

@Entity(primaryKeys = ["movieId", "genreId"])
data class MovieAndGenreCrossRef(
    val movieId: Int,
    val genreId: Int
)
