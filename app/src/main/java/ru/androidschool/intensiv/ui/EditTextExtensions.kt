package ru.androidschool.intensiv.ui

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.subjects.PublishSubject

fun EditText.afterTextChanged(action: (s: Editable?) -> Unit) =
    addTextChangedListener(afterTextChanged = action)

fun EditText.onTextChangedObservable(): Observable<String> {
    return Observable.create(ObservableOnSubscribe<String> { emitter ->
        this.doAfterTextChanged { text ->
            emitter.onNext(text.toString())
        }
    })
}

fun EditText.onTextChangedPublishSubject(): PublishSubject<String> {
    val searchPublishSubject: PublishSubject<String> = PublishSubject.create()

    this.doAfterTextChanged { text ->
        searchPublishSubject.onNext(text.toString())
    }

    return searchPublishSubject
}
