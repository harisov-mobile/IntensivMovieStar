package ru.androidschool.intensiv.data.repository

import android.content.Context
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.MovieDatabase
import ru.androidschool.intensiv.data.dbo.MovieAndGenreAndActorAndProductionCompany
import ru.androidschool.intensiv.domain.repository.MovieRepository
import ru.androidschool.intensiv.utils.ViewFeature

class MovieRepositoryLocal private constructor(context: Context) : MovieRepository {

    private val movieDao = MovieDatabase.get(context.applicationContext).movieDao()

    companion object {
        private var instance: MovieRepositoryLocal? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = MovieRepositoryLocal(context)
            }
        }

        fun get(): MovieRepositoryLocal {
            return instance ?: throw IllegalStateException("FavoriteMovieRepositoryLocal is not initialized!")
        }
    }

    override fun getMovies(viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>> {
        return movieDao.getMovies(ViewFeature.FAVORITE)
    }
}
