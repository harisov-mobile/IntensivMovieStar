package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.TvShowDBO
import ru.androidschool.intensiv.data.dto.TvShowDetails

object TvShowMapper {
    fun toTvShowDBO(tvShowDetails: TvShowDetails): TvShowDBO {
        return TvShowDBO(
            id = tvShowDetails.id,
            name = tvShowDetails.name
        )
    }
}