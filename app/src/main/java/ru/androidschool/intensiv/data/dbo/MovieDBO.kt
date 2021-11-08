package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.androidschool.intensiv.BuildConfig

@Entity(tableName = "movies")
data class MovieDBO(
    @PrimaryKey
    val movieId: Int,
    val title: String,
    val overview: String,
    val voteAverage: Double,
    val releaseDate: String,
    val posterPath: String
) {
    fun getRating(): Float = voteAverage.div(2).toFloat()

    fun getPoster(): String = "${BuildConfig.IMAGE_URL}$posterPath"
}

