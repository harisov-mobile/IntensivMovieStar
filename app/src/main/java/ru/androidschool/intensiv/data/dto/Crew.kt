package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Crew(
    @SerializedName("adult") val isAdult: Boolean,
    @SerializedName("gender") val gender: Int? = null, // согласно документации integer or null
    @SerializedName("id") val id: Int,
    @SerializedName("known_for_department") val knownForDepartment: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("department") val department: String,
    @SerializedName("job") val job: String
) {
    @SerializedName("profile_path")
    val profilePath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
