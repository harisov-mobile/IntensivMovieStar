package ru.androidschool.intensiv.data

import java.util.*

// класс, обозначающий жанр

data class Genre(
    val id: UUID = UUID.randomUUID(),
    var name: String = ""
)
