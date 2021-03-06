package ru.androidschool.intensiv.data.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.androidschool.intensiv.data.dbo.*
import ru.androidschool.intensiv.utils.ViewFeature

@Dao
interface MovieDao {

    // TvShow
    @Query("SELECT * FROM TvShows")
    fun getTvShows(): Observable<List<TvShowDBO>>

    @Query("SELECT * FROM TvShows WHERE id=:id")
    fun getTvShow(id: Int): Observable<TvShowDBO?>

    @Insert
    fun insert(tvShowDBO: TvShowDBO): Completable

    @Delete
    fun delete(tvShowDBO: TvShowDBO): Completable

    @Update
    fun update(tvShowDBO: TvShowDBO): Completable

    // Movie
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieDBO): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieDBO>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(genre: GenreDBO): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genres: List<GenreDBO>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(actor: ActorDBO): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActors(actors: List<ActorDBO>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productionCompany: ProductionCompanyDBO): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductionCompanies(productionCompanies: List<ProductionCompanyDBO>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenreJoins(joins: List<MovieAndGenreCrossRef>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActorJoins(joins: List<MovieAndActorCrossRef>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductionCompanyJoins(joins: List<MovieAndProductionCompanyCrossRef>): Completable

    @Delete
    fun delete(movieDBO: MovieDBO): Completable

    @Transaction
    @Query("SELECT * FROM movies WHERE movieId = :movieId")
    fun getMovie(movieId: Int): Observable<MovieAndGenreAndActorAndProductionCompany>

    @Transaction
    @Query("SELECT * FROM movies WHERE viewFeature = :viewFeature")
    fun getMovies(viewFeature: ViewFeature): Single<List<MovieAndGenreAndActorAndProductionCompany>>

    @Query("DELETE FROM movies WHERE viewFeature = :viewFeature")
    fun deleteViewFeaturedMovies(viewFeature: ViewFeature): Completable

    @Transaction
    @Query("SELECT * FROM movies WHERE movieId = :movieId AND viewFeature = :viewFeature")
    fun getFavoriteMovie(movieId: Int, viewFeature: ViewFeature): Single<MovieAndGenreAndActorAndProductionCompany>
}
