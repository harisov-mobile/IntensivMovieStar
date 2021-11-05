package ru.androidschool.intensiv.ui

import android.view.View
import android.widget.ProgressBar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.applySchedulers(): Single<T> =
    this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.applyProgressBar(progress_bar: ProgressBar): Single<T> =
    this
        .doOnSubscribe { progress_bar.visibility = View.VISIBLE }
        .doFinally { progress_bar.visibility = View.GONE }
