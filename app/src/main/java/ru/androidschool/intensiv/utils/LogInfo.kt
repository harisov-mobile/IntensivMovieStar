package ru.androidschool.intensiv.utils

import ru.androidschool.intensiv.BuildConfig
import timber.log.Timber

class LogInfo {
    companion object {
        fun errorInfo(error: Throwable, message: String) {
            if (BuildConfig.DEBUG) {
                // только в случае режима отладки
                Timber.e(error, message)
            }
        }
    }
}
