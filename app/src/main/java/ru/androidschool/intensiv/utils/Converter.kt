package ru.androidschool.intensiv.utils

import ru.androidschool.intensiv.data.TvShowDBO
import ru.androidschool.intensiv.data.TvShowDetails

class Converter {

    companion object {
        fun toTvShowDBO(tvShowDetails: TvShowDetails): TvShowDBO {
            return TvShowDBO(
                id = tvShowDetails.id,
                name = tvShowDetails.name
            )
        }
    }


}