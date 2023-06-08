package com.example.myproducts.data.api

import com.example.myproducts.data.model.AddProductResponse
import com.example.myproducts.data.model.Products
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiServices {

    @GET("api/public/get")
    suspend fun getProducts(): Products

    @Multipart
    @POST("api/public/add")
    suspend fun addProduct(
        @PartMap partMap: HashMap<String, RequestBody>,
        @Part files: List<MultipartBody.Part?>
    ) : AddProductResponse
}