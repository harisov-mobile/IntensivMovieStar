package ru.androidschool.intensiv.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.androidschool.intensiv.data.dbo.*
import ru.androidschool.intensiv.utils.ViewFeature

interface MovieRepositoryDB {
    fun getMovies(viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>>

    fun delete(movieDBO: MovieDBO): Completable

    fun insert(movie: MovieDBO): Completable

    fun insertGenres(genres: List<GenreDBO>): Completable

    fun insertActors(actors: List<ActorDBO>): Completable

    fun insertProductionCompanies(productionCompanies: List<ProductionCompanyDBO>): Completable

    fun insertGenreJoins(joins: List<MovieAndGenreCrossRef>): Completable

    fun insertActorJoins(joins: List<MovieAndActorCrossRef>): Completable

    fun insertProductionCompanyJoins(joins: List<MovieAndProductionCompanyCrossRef>): Completable

    fun getFavoriteMovie(movieId: Int, viewFeature: ViewFeature): Single<MovieAndGenreAndActorAndProductionCompany>
}
