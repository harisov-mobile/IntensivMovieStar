package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Season(
    @SerializedName("air_date") val airDate: String,
    @SerializedName("episode_count") val episodeCount: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("season_number") val seasonNumber: Int
) {
    @SerializedName("poster_path")
    val posterPath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
