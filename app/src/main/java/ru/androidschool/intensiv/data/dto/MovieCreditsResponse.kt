package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.Crew

data class MovieCreditsResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<Actor>,
    @SerializedName("crew") val crew: List<Crew>
)
