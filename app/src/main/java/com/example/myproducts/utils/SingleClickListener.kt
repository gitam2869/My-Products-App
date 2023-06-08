package com.example.myproducts.utils

import android.os.SystemClock
import android.util.Log
import android.view.View

class SingleClickListener(
    private val intervalMillis: Long = 500,
    private val doClick: ((View) -> Unit)
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0

    override fun onClick(v: View?) {
        if ((SystemClock.elapsedRealtime() - lastTimeClicked) >= intervalMillis) {
            v?.let { doClick(it) }
            lastTimeClicked = SystemClock.elapsedRealtime()
        }
    }
}