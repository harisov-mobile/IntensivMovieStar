package ru.androidschool.intensiv.data.dbo

import androidx.room.Entity
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.utils.ViewFeature

@Entity(tableName = "movies", primaryKeys = ["movieId", "viewFeature"])
data class MovieDBO(
    val movieId: Int,
    val title: String,
    val overview: String,
    val voteAverage: Double,
    val releaseDate: String,
    val posterPath: String? = null,
    val viewFeature: ViewFeature
) {
    fun getRating(): Float = voteAverage.div(2).toFloat()

    fun getPosterPathWithImageUrl(): String = "${BuildConfig.IMAGE_URL}$posterPath"
}
