package ru.androidschool.intensiv.data

import java.util.*

class Movie(
    var title: String? = "",
    var voteAverage: Double = 0.0,
    // добавлены поля
    var overview: String = "",
    var studio: String = "",
    var releaseDate: Date? = null,
    var previewUrl: String? = "",
    var genreList: List<Genre> = emptyList(), // список жанров
    var actorList: List<Actor> = emptyList() // список актеров-исполнителей (актерский состав)

) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
