package com.example.myproducts.data.repository

import android.util.Log
import com.example.myproducts.data.api.ApiServices
import com.example.myproducts.utils.NetworkResult
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class ProductRepository constructor(private val apiServices: ApiServices) {

    suspend fun getProducts(): NetworkResult<*> {
        return try {
            val response = apiServices.getProducts()
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(e.message(), null)
        } catch (e: IOException) {
            NetworkResult.Error(e.message ?: "", null)
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "", null)
        }
    }
}