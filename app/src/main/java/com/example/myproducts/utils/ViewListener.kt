package com.example.myproducts.utils

import android.view.View

class ViewListener {
    companion object {
        fun View.setOnSingleClickListener(
            intervalMillis: Long = Constants.SINGLE_CLICK_EVENT_DEBOUNCE_TIME,
            doClick: (View) -> Unit
        ) = setOnClickListener(SingleClickListener(intervalMillis, doClick))
    }
}