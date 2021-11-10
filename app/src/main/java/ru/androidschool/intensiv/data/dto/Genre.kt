package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName

// класс, обозначающий жанр

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
