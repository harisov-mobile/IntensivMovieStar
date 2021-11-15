package ru.androidschool.intensiv.data.dbo

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieAndGenreAndActorAndProductionCompany(
    @Embedded
    val movieDBO: MovieDBO,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "genreId",
        associateBy = Junction(
            MovieAndGenreCrossRef::class,
            parentColumn = "movieId",
            entityColumn = "genreId"
        )
    )
    val genres: List<GenreDBO>,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "actorId",
        associateBy = Junction(
            MovieAndActorCrossRef::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<ActorDBO>,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "productionCompanyId",
        associateBy = Junction(
            MovieAndProductionCompanyCrossRef::class,
            parentColumn = "movieId",
            entityColumn = "productionCompanyId"
        )
    )
    val productionCompanies: List<ProductionCompanyDBO>
)
