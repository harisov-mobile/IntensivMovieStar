package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Movie(
    @SerializedName("adult") val isAdult: Boolean,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()

    @SerializedName("poster_path")
    val posterPath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"

    @SerializedName("backdrop_path")
    val backdropPath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
