package com.example.myproducts.utils

import android.view.View

object ViewExtension {

    fun View.visible(){
        visibility = View.VISIBLE
    }

    fun View.invisible(){
        visibility = View.INVISIBLE
    }

    fun View.gone(){
        visibility = View.GONE
    }


    fun View.disable() {
        isEnabled = false
        alpha = 0.6f
    }

    fun View.enable() {
        isEnabled = true
        alpha = 1.0f
    }
}