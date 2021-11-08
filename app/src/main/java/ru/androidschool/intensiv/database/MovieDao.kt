package ru.androidschool.intensiv.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Observable
import ru.androidschool.intensiv.data.TvShowDBO

@Dao
interface MovieDao {

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

}