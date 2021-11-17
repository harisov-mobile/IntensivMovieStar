package ru.androidschool.intensiv.data.repository

import android.content.Context
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.MovieDatabase
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.domain.repository.MovieRepository
import ru.androidschool.intensiv.utils.ViewFeature

object MovieRepositoryImpl : MovieRepository {
    override fun getMovies(context: Context, viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>> {
        val movieDao = MovieDatabase.get(context).movieDao()
        return movieDao.getMovies(viewFeature)
    }
}
