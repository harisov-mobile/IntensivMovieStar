package ru.androidschool.intensiv.ui

import android.view.View
import android.widget.ProgressBar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.applySchedulers(): Observable<T> =
    this
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.applyProgressBar(progress_bar: ProgressBar): Observable<T> =
    this
        .doOnSubscribe { progress_bar.visibility = View.VISIBLE }
        .doFinally { progress_bar.visibility = View.GONE }
