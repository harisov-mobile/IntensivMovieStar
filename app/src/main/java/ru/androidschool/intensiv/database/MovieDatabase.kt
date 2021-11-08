package ru.androidschool.intensiv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.androidschool.intensiv.data.dbo.*

@Database(entities = [MovieDBO::class, GenreDBO::class, ActorDBO::class, ProductionCompanyDBO::class,
    MovieAndGenreCrossRef::class, MovieAndActorCrossRef::class, MovieAndProductionCompanyCrossRef::class,
    TvShowDBO::class],
    version = 1,
    exportSchema = false)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        private val DATABASE_NAME = "moviedatabase.db"
        private var instance: MovieDatabase? = null

        @Synchronized
        fun get(context: Context): MovieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    DATABASE_NAME)
                    //.allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

}