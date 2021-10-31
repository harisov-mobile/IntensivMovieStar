package ru.androidschool.intensiv.ui

import android.text.Editable
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

fun EditText.afterTextChanged(action: (s: Editable?) -> Unit) =
    addTextChangedListener(afterTextChanged = action)

fun EditText.onTextChangedObservable(): Observable<String> {
    return Observable.create(ObservableOnSubscribe<String> { emitter ->
        this.doAfterTextChanged { text ->
            emitter.onNext(text.toString())
        }
    })
}
