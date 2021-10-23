package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class Movie (

    @SerializedName("adult")
    val isAdult : Boolean,

    @SerializedName("backdrop_path")
    val backdropPath : String?,  // согласно документации string or null

    @SerializedName("genre_ids")
    val genreIds : List<Int>,

    @SerializedName("id")
    val id : Int,

    @SerializedName("original_language")
    val originalLanguage : String,

    @SerializedName("original_title")
    val originalTitle : String,

    @SerializedName("overview")
    val overview : String,

    @SerializedName("popularity")
    val popularity : Double,

    @SerializedName("poster_path")
    val posterPath : String?,  // согласно документации string or null

    @SerializedName("release_date")
    val releaseDate : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("video")
    val video : Boolean,

    @SerializedName("vote_average")
    val voteAverage : Double,

    @SerializedName("vote_count")
    val voteCount : Int
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}

//data class Movie(
//
////    var title: String? = "",
////    var voteAverage: Double = 0.0,
////    // добавлены поля
////    var overview: String = "",
////    var studio: String = "",
////    var releaseDate: Date? = null,
////    var previewUrl: String? = "",
////    var genreList: List<Genre> = emptyList(), // список жанров
////    var actorList: List<Actor> = emptyList() // список актеров-исполнителей (актерский состав)
//
//) {
//    val rating: Float
//        get() = voteAverage.div(2).toFloat()
//}
