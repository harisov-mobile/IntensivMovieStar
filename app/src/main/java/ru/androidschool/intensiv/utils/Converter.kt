package ru.androidschool.intensiv.utils

import ru.androidschool.intensiv.data.*
import ru.androidschool.intensiv.data.dbo.*

class Converter {

    companion object {
        fun toTvShowDBO(tvShowDetails: TvShowDetails): TvShowDBO {
            return TvShowDBO(
                id = tvShowDetails.id,
                name = tvShowDetails.name
            )
        }

        fun toMovieDBO(movieDetails: MovieDetails): MovieDBO {
            return MovieDBO(
                movieId = movieDetails.id,
                title = movieDetails.title,
                overview = movieDetails.overview,
                voteAverage = movieDetails.voteAverage,
                releaseDate = movieDetails.releaseDate,
                posterPath = movieDetails.posterPath
            )
        }

        fun toGenreDBOList(genres: List<Genre>): List<GenreDBO> {
            return genres.map { toGenreDBO(it) }
        }

        fun toGenreDBO(genre: Genre): GenreDBO {
            return GenreDBO(
                genreId = genre.id,
                name = genre.name
            )
        }

        fun toActorDBOList(actors: List<Actor>): List<ActorDBO> {
            return actors.map { toActorDBO(it) }
        }

        fun toActorDBO(actor: Actor): ActorDBO {
            return ActorDBO(
                actorId = actor.id,
                name = actor.name,
                profilePath = actor.profilePath
            )
        }

        fun toProductionCompanyDBOList(productionCompanies: List<ProductionCompany>): List<ProductionCompanyDBO> {
            return productionCompanies.map { toProductionCompanyDBO(it) }
        }

        fun toProductionCompanyDBO(productionCompany: ProductionCompany): ProductionCompanyDBO {
            return ProductionCompanyDBO(
                productionCompanyId = productionCompany.id,
                name = productionCompany.name
            )
        }

        fun toMovieAndGenreCrossRefList(movieId: Int, genresDBO: List<GenreDBO>): List<MovieAndGenreCrossRef> {
            return genresDBO.map { toMovieAndGenreCrossRef(movieId, it.genreId) }
        }

        fun toMovieAndGenreCrossRef(movieId: Int, genreId: Int): MovieAndGenreCrossRef {
            return MovieAndGenreCrossRef(
                movieId = movieId,
                genreId = genreId
            )
        }

        fun toMovieAndActorCrossRefList(movieId: Int, actorsDBO: List<ActorDBO>): List<MovieAndActorCrossRef> {
            return actorsDBO.map { toMovieAndActorCrossRef(movieId, it.actorId) }
        }

        fun toMovieAndActorCrossRef(movieId: Int, actorId: Int): MovieAndActorCrossRef {
            return MovieAndActorCrossRef(
                movieId = movieId,
                actorId = actorId
            )
        }

        fun toMovieAndProductionCompanyCrossRefList(
            movieId: Int,
            productionCompaniesDBO: List<ProductionCompanyDBO>
        ): List<MovieAndProductionCompanyCrossRef> {
            return productionCompaniesDBO.map { toMovieAndProductionCompanyCrossRef(movieId, it.productionCompanyId) }
        }

        fun toMovieAndProductionCompanyCrossRef(
            movieId: Int,
            productionCompanyId: Int
        ): MovieAndProductionCompanyCrossRef {
            return MovieAndProductionCompanyCrossRef(
                movieId = movieId,
                productionCompanyId = productionCompanyId
            )
        }
    }
}