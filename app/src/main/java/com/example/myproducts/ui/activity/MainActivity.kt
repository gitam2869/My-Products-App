package com.example.myproducts.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.myproducts.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun show(message: String, duration: Int = 0) {
        hide()
        snackbar = Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_SHORT)
        snackbar?.duration = duration
        snackbar?.show()
    }

    fun hide() {
        snackbar?.dismiss()
        snackbar = null
    }

    override fun onStop() {
        super.onStop()
        hide()
    }
}