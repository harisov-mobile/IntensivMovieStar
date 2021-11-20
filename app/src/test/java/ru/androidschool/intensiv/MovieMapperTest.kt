package ru.androidschool.intensiv

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.mappers.MovieMapper
import ru.androidschool.intensiv.data.vo.MovieVO
import ru.androidschool.intensiv.utils.ViewFeature

class MovieMapperTest {

    private lateinit var movieDTO: Movie
    private lateinit var correctMovieVO: MovieVO
    private lateinit var movieVO: MovieVO

    @Before
    fun setUp() {
        movieDTO = Movie(
            isAdult = true,
            genreIds = emptyList(),
            id = 1,
            originalLanguage = "2",
            originalTitle = "3",
            overview = "4",
            popularity = 5.5,
            releaseDate = "6",
            title = "7",
            video = true,
            voteAverage = 8.8,
            voteCount = 9,
            posterPath = "10"
        )

        correctMovieVO = MovieVO(
            genres = emptyList(),
            id = 1,
            overview = "4",
            releaseDate = "6",
            title = "7",
            rating = 4.4f,
            posterPath = "10",
            productionCompanies = emptyList(),
            actors = emptyList(),
            viewFeature = ViewFeature.FAVORITE
        )

    }

    @Test
    fun checkMappingMovieFromDtoToVo() {
        movieVO = MovieMapper.toMovieVO(movieDTO, ViewFeature.FAVORITE)
        assertEquals(correctMovieVO, movieVO)
    }
}