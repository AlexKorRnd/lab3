package com.alexkorrnd.pmulab3

import android.os.Bundle
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimerManager(savedState: Bundle?, val callback: Callback) {

    interface Callback {
        fun onTimeChanged()
    }

    companion object {
        private const val ARG_CUR_TIME = "ARG_CUR_TIME"
        private const val ARG_IS_RUNNING = "ARG_IS_RUNNING"
    }

    var currentTimeInSec: Long = 0
        private set

    var isRunning: Boolean = false
        private set

    private var subscription: Subscription? = null

    init {
        currentTimeInSec = savedState?.getLong(ARG_CUR_TIME, 0) ?: 0
        isRunning = savedState?.getBoolean(ARG_IS_RUNNING, false) ?: false
    }

    fun start() {
        isRunning = true
        subscription =
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            currentTimeInSec++
                            callback.onTimeChanged()
                        }
    }

    fun stop() {
        isRunning = false
        subscription?.unsubscribe()
        subscription = null
    }

    fun reset() {
        currentTimeInSec = 0
    }

    fun saveState(): Bundle =
            Bundle().apply {
                putLong(ARG_CUR_TIME, currentTimeInSec)
                putBoolean(ARG_IS_RUNNING, isRunning)
            }

    fun getFormattedTime(): String =
            "${currentTimeInSec / 3600}:${currentTimeInSec % 3600 / 60}:${currentTimeInSec % 60}"

}
