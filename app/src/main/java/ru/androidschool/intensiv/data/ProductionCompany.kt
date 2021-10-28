package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.BuildConfig

data class ProductionCompany(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val originCountry: String
) {
    @SerializedName("logo_path")
    val logoPath: String? = null // согласно документации string or null
        get() = "${BuildConfig.IMAGE_URL}$field"
}
