package ru.androidschool.intensiv.data.vo

import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.dto.Actor
import ru.androidschool.intensiv.data.dto.Genre
import ru.androidschool.intensiv.data.dto.ProductionCompany
import ru.androidschool.intensiv.utils.ViewFeature

data class MovieVO(
    val id: Int,
    val title: String,
    val rating: Float,
    val posterPath: String? = null,
    val overview: String,
    val productionCompanies: List<ProductionCompany>,
    val genres: List<Genre>,
    val releaseDate: String,
    val actors: List<Actor>,
    val viewFeature: ViewFeature
) {
    fun getPosterPathWithImageUrl() = "${BuildConfig.IMAGE_URL}$posterPath"
}