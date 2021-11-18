package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.GenreDBO
import ru.androidschool.intensiv.data.dto.Genre

object GenreMapper : ViewObjectMapper<GenreDBO, Genre> {
    override fun toViewObject(genre: Genre): GenreDBO {
        return GenreDBO(
            genreId = genre.id,
            name = genre.name
        )
    }
}
