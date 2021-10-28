package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

class TvShow(
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val originCountry: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
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
