package ru.androidschool.intensiv.database

import androidx.room.TypeConverter
import ru.androidschool.intensiv.utils.ViewFeature

class MovieFinderAppTypeConverters {
    @TypeConverter
    fun toViewFeature(stringViewFeature: String): ViewFeature {
        return ViewFeature.valueOf(stringViewFeature)
    }

    @TypeConverter
    fun fromViewFeature(viewFeature: ViewFeature): String {
        return viewFeature.name
    }
}
