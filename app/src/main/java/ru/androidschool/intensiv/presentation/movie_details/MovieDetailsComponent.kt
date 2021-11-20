package ru.androidschool.intensiv.presentation.movie_details

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MovieDetailsModule::class])
interface MovieDetailsComponent {
    fun inject(movieDetailsFragment: MovieDetailsFragment)
}
