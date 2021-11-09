package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class TvShowDetails(
    @SerializedName("created_by") val createdBy: List<CreatedBy>,
    @SerializedName("episode_run_time") val episodeRunTime: List<Int>,
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("id") val id: Int,
    @SerializedName("in_production") val inProduction: Boolean,
    @SerializedName("languages") val languages: List<String>,
    @SerializedName("last_air_date") val lastAirDate: String,
    @SerializedName("last_episode_to_air") val lastEpisodeToAir: LastEpisodeToAir,
    @SerializedName("name") val name: String,
    @SerializedName("next_episode_to_air") val nextEpisodeToAir: String,
    @SerializedName("networks") val networks: List<Network>,
    @SerializedName("number_of_episodes") val numberOfEpisodes: Int,
    @SerializedName("number_of_seasons") val numberOfSeasons: Int,
    @SerializedName("origin_country") val originCountry: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("seasons") val seasons: List<Season>,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("type") val type: String,
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
