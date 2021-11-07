package ru.androidschool.intensiv.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TvShows")
data class TvShowDBO(
    @PrimaryKey
    val id: Int,
    val name: String
)
