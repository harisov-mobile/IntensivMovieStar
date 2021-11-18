package ru.androidschool.intensiv.domain.repository

import android.content.Context
import io.reactivex.Single
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.utils.ViewFeature

interface MovieRepository {
    fun getMovies(context: Context, viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>>
}
