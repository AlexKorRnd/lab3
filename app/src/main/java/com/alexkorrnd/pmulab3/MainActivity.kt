package com.alexkorrnd.pmulab3

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import biz.growapp.base.extensions.hide
import biz.growapp.base.extensions.show
import org.jetbrains.anko.find
import org.jetbrains.anko.onCheckedChange
import org.jetbrains.anko.onClick

class MainActivity: AppCompatActivity(), TimerManager.Callback {

    companion object {
        private const val ARG_TIME_MANAGER = "ARG_TIME_MANAGER"
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var tvTime: TextView
    private lateinit var tbStatus: ToggleButton
    private lateinit var btnReset: Button

    private lateinit var timeManager: TimerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTime = find(R.id.tvTime)
        tbStatus = find(R.id.tbStatus)
        btnReset = find(R.id.btnReset)

        timeManager = TimerManager(savedInstanceState?.getBundle(ARG_TIME_MANAGER), this)
        tvTime.text = timeManager.getFormattedTime()

        tbStatus.onCheckedChange { compoundButton, isChecked ->
            Log.d(TAG, "onCheckedChange:: isChecked = $isChecked")
            if (isChecked) {
                timeManager.start()
                btnReset.show()
            } else {
                timeManager.stop()
            }
        }
        btnReset.onClick {
            timeManager.reset()
            btnReset.hide()
        }

        Log.d(TAG, "onCreate:: timeManager.isRunning = ${timeManager.isRunning}")
        tbStatus.isChecked = timeManager.isRunning
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle(ARG_TIME_MANAGER, timeManager.saveState())
        Log.d(TAG, "onSaveInstanceState:: timeManager.isRunning = ${timeManager.isRunning}")
        super.onSaveInstanceState(outState)
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        tbStatus.isChecked = false
        super.onStop()
    }

    override fun onTimeChanged() {
        tvTime.text = timeManager.getFormattedTime()
    }
}
