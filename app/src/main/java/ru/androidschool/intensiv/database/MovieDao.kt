package ru.androidschool.intensiv.database

import androidx.room.*
import ru.androidschool.intensiv.data.TvShowDBO

@Dao
interface MovieDao {

    @Query("SELECT * FROM TvShows")
    fun getTvShows(): List<TvShowDBO>

    @Query("SELECT * FROM TvShows WHERE id=:id")
    fun getTvShow(id: Int): TvShowDBO

    @Insert
    fun insert(tvShowDBO: TvShowDBO)

    @Delete
    fun delete(tvShowDBO: TvShowDBO)

    @Update
    fun update(tvShowDBO: TvShowDBO)

}