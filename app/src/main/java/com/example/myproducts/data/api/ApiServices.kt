package com.example.myproducts.data.api

import com.example.runningtrackerapp.data.model.Products
import retrofit2.http.GET

interface ApiServices {

    @GET("api/public/get")
    suspend fun getProducts(): Products
}