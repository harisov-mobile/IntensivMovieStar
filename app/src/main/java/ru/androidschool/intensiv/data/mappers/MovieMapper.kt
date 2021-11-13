package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dbo.*
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.vo.MovieVO
import ru.androidschool.intensiv.utils.ViewFeature

object MovieMapper {
    fun toMovieDBO(movieDetails: MovieDetails, viewFeature: ViewFeature): MovieDBO {
        return MovieDBO(
            movieId = movieDetails.id,
            title = movieDetails.title,
            overview = movieDetails.overview,
            voteAverage = movieDetails.voteAverage,
            releaseDate = movieDetails.releaseDate,
            posterPath = movieDetails.posterPath,
            viewFeature = viewFeature
        )
    }

    fun toMovieDBO(movie: Movie, viewFeature: ViewFeature): MovieDBO {
        return MovieDBO(
            movieId = movie.id,
            title = movie.title,
            overview = movie.overview,
            voteAverage = movie.voteAverage,
            releaseDate = movie.releaseDate,
            posterPath = movie.posterPath,
            viewFeature = viewFeature
        )
    }

    fun toMovieVO(movie: Movie, viewFeature: ViewFeature): MovieVO {
        // из ретрофитовского Movie преобразуем в вью-объект MovieVO
        return MovieVO(
            id = movie.id,
            title = movie.title,
            rating = movie.rating,
            posterPath = movie.posterPath,
            overview = movie.overview,
            productionCompanies = emptyList(),
            genres = emptyList(),
            releaseDate = movie.releaseDate,
            actors = emptyList(),
            viewFeature = viewFeature
        )
    }

    fun toMovieVO(movieDBO: MovieDBO, viewFeature: ViewFeature): MovieVO {
        // из БД MovieDBO преобразуем в вью-объект MovieVO
        return MovieVO(
            id = movieDBO.movieId,
            title = movieDBO.title,
            rating = movieDBO.getRating(),
            posterPath = movieDBO.posterPath,
            overview = movieDBO.overview,
            productionCompanies = emptyList(),
            genres = emptyList(),
            releaseDate = movieDBO.releaseDate,
            actors = emptyList(),
            viewFeature = viewFeature
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