package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class BelongsToCollection (
    @SerializedName("name") val name : String
) {
    @SerializedName("poster_path")
    val posterPath : String? = null  // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"

    @SerializedName("backdrop_path")
    val backdropPath : String? = null  // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}