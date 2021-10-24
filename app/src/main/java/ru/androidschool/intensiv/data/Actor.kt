package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

// класс, обозначающий актера

data class Actor(
    @SerializedName("adult") val isAdult : Boolean,
    @SerializedName("gender") val gender : Int? = null,
    @SerializedName("id") val id : Int,
    @SerializedName("known_for_department") val knownForDepartment : String,
    @SerializedName("name") val name : String,
    @SerializedName("original_name") val originalName : String,
    @SerializedName("popularity") val popularity : Double,
    @SerializedName("character") val character : String,
    @SerializedName("credit_id") val creditId : String,
    @SerializedName("order") val order : Int
) {
    @SerializedName("profile_path")
    val profilePath : String? = null  // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
