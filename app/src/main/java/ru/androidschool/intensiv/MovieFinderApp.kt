package ru.androidschool.intensiv

import android.app.Application
import ru.androidschool.intensiv.data.repository.MovieRepositoryLocal
import ru.androidschool.intensiv.presentation.movie_details.MovieDetailsComponent
import ru.androidschool.intensiv.presentation.movie_details.MovieDetailsModule
import timber.log.Timber

class MovieFinderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDebugTools()

        MovieRepositoryLocal.initialize(this)
    }
    private fun initDebugTools() {
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        var instance: MovieFinderApp? = null
            private set
    }
}
