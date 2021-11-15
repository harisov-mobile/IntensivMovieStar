package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres")
data class GenreDBO(
    @PrimaryKey
    val genreId: Int,
    val name: String
)
