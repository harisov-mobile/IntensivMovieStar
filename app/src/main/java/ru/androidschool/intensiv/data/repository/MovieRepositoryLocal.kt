package ru.androidschool.intensiv.data.repository

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Single
import ru.androidschool.intensiv.data.database.MovieDatabase
import ru.androidschool.intensiv.data.dbo.*
import ru.androidschool.intensiv.domain.repository.MovieRepositoryDB
import ru.androidschool.intensiv.utils.ViewFeature

class MovieRepositoryLocal private constructor(context: Context) : MovieRepositoryDB {

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

    override fun delete(movieDBO: MovieDBO): Completable {
        return movieDao.delete(movieDBO)
    }

    override fun insert(movie: MovieDBO): Completable {
        return movieDao.insert(movie)
    }

    override fun insertGenres(genres: List<GenreDBO>): Completable {
        return movieDao.insertGenres(genres)
    }

    override fun insertActors(actors: List<ActorDBO>): Completable {
        return movieDao.insertActors(actors)
    }

    override fun insertProductionCompanies(productionCompanies: List<ProductionCompanyDBO>): Completable {
        return movieDao.insertProductionCompanies(productionCompanies)
    }

    override fun insertGenreJoins(joins: List<MovieAndGenreCrossRef>): Completable {
        return movieDao.insertGenreJoins(joins)
    }

    override fun insertActorJoins(joins: List<MovieAndActorCrossRef>): Completable {
        return movieDao.insertActorJoins(joins)
    }

    override fun insertProductionCompanyJoins(joins: List<MovieAndProductionCompanyCrossRef>): Completable {
        return movieDao.insertProductionCompanyJoins(joins)
    }

    override fun getFavoriteMovie(
        movieId: Int,
        viewFeature: ViewFeature
    ): Single<MovieAndGenreAndActorAndProductionCompany> {
        return movieDao.getFavoriteMovie(movieId, ViewFeature.FAVORITE)
    }
}
