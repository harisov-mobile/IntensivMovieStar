package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.GenreDBO
import ru.androidschool.intensiv.data.dto.Genre

object GenreMapper {
    fun toGenreDBOList(genres: List<Genre>): List<GenreDBO> {
        return genres.map { toGenreDBO(it) }
    }

    fun toGenreDBO(genre: Genre): GenreDBO {
        return GenreDBO(
            genreId = genre.id,
            name = genre.name
        )
    }
}
