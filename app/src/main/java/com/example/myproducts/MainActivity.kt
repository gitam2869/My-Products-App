package com.example.myproducts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myproducts.databinding.ActivityMainBinding
import com.example.myproducts.utils.ApiResult
import com.example.myproducts.viewmodel.ProductListViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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