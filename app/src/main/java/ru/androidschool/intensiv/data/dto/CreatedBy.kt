package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class CreatedBy(
    @SerializedName("id") val id: Int,
    @SerializedName("credit_id") val credit_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("gender") val gender: Int
) {
    @SerializedName("profile_path")
    val profilePath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
