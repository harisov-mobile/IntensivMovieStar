package ru.androidschool.intensiv.data

import java.util.*

// класс, обозначающий актера

data class Actor(
    val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var surname: String = ""
)
