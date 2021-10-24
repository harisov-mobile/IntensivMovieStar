package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class TvShowResponse (
    @SerializedName("page")
    val page : Int,

    @SerializedName("results")
    val results : List<TvShow>,

    @SerializedName("total_pages")
    val totalPages : Int,

    @SerializedName("total_results")
    val totalResults : Int
)