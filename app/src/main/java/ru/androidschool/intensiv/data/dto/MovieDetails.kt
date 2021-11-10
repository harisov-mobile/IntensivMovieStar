package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class MovieDetails(
    @SerializedName("adult") val isAdult: Boolean,
    @SerializedName("belongs_to_collection") val belongsToCollection: BelongsToCollection,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Int,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("poster_path") val posterPath: String? = null // согласно документации string or null

) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()

    @SerializedName("backdrop_path")
    val backdropPath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"

    fun getPosterPathWithImageUrl() = "${BuildConfig.IMAGE_URL}$posterPath"
}
