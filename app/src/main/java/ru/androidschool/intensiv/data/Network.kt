package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class Network (
    @SerializedName("name") val name : String,
    @SerializedName("id") val id : Int,
    @SerializedName("origin_country") val originCountry : String
) {
    @SerializedName("logo_path")
    val logoPath : String? = null  // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}