package ru.androidschool.intensiv.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.feed_header.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.android.synthetic.main.search_toolbar.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.mappers.MovieMapper
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.vo.MovieVO
import ru.androidschool.intensiv.presentation.feed.MovieItem
import ru.androidschool.intensiv.ui.applyProgressBar
import ru.androidschool.intensiv.ui.applySchedulers
import ru.androidschool.intensiv.ui.onTextChangedPublishSubject
import ru.androidschool.intensiv.utils.Const
import ru.androidschool.intensiv.utils.ViewFeature
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>()
    }

    private lateinit var compositeDisposable: CompositeDisposable

    private val options = navOptions {
        anim {
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable = CompositeDisposable()

        // считываем аргументы (учебный комментарий, в реальном проекте такого комментария не будет)
        val searchTerm = requireArguments().getString(KEY_SEARCH)
        search_toolbar.setText(searchTerm)
        movies_recycler_view.adapter = adapter
        searchTerm?.let {
            displaySearchedMovies(it)
        } ?: let {
            adapter.clear()
        }

        // слушаем нажатия в EditText через PublishSubject (учебный комментарий, в реальном проекте такого комментария не будет)
        trackSearchInputBySubject()
    }

    private fun trackSearchInputBySubject() {

        val searchPublishSubject: PublishSubject<String> = search_edit_text.onTextChangedPublishSubject()
        val searchDisposable = searchPublishSubject
            .map { it.trim() } // удалить все пробелы (учебный комментарий, в реальном проекте такого комментария не будет)
            .filter { it.length > MIN_LENGTH } // Длина слова должна быть > 3 символов (учебный комментарий, в реальном проекте такого комментария не будет)
            .debounce(SEARCH_DELAY_MILLISEC, TimeUnit.MILLISECONDS) // Отправлять введёное слово не раньше 0.5 секунды с момента окончания ввода (учебный комментарий, в реальном проекте такого комментария не будет)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    searchTerm -> searchTerm?.let {
                        displaySearchedMovies(searchTerm)
                    }
                },
                {
                    error -> Timber.e(error, "Ошибка при поиске фильмов")
                }
            )

        compositeDisposable.add(searchDisposable)
    }

    private fun displaySearchedMovies(searchTerm: String) {
        adapter.clear()
        // Получаем список найденных фильмов
        val singleSearchedMovies = MovieApiClient.apiClient.getSearchedMovies(query = searchTerm)
        val disposableSearchedMovies = singleSearchedMovies
            .applySchedulers()
            .applyProgressBar(progress_bar)
            .subscribe(
                {
                    response ->
                    val movieResultList = response.results
                    val movieList = movieResultList.map { movie -> MovieMapper.toMovieVO(movie, ViewFeature.SEARCHED) }
                        .map { movieVO ->
                            MovieItem(movieVO) {
                                openMovieDetails(
                                    it
                                )
                            }
                        }.toList()
                        adapter.apply { addAll(movieList) }
                },
                {
                    error -> Timber.e(error, "Ошибка при поиске фильмов")
                }
            )
        compositeDisposable.add(disposableSearchedMovies)
    }

    private fun openMovieDetails(movieVO: MovieVO) {
        val bundle = Bundle()
        bundle.putInt(Const.KEY_ID, movieVO.id)
        findNavController().navigate(R.id.movie_details_fragment, bundle, options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    companion object {
        const val MIN_LENGTH = 3
        const val KEY_SEARCH = "search"
        const val SEARCH_DELAY_MILLISEC = 500L
    }
}
