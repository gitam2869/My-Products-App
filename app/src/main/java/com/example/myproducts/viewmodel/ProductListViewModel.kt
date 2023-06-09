package com.example.myproducts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproducts.data.repository.ProductRepository
import com.example.myproducts.utils.ApiResult
import com.example.myproducts.utils.NetworkResult
import com.example.myproducts.data.model.Products
import kotlinx.coroutines.launch

class ProductListViewModel constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    var response: MutableLiveData<ApiResult<Products>> = MutableLiveData()

    suspend fun getProducts() {
        response.value = ApiResult.Loading(true)
        when (val result = productRepository.getProducts()) {
            is NetworkResult.Success -> {
                response.value = ApiResult.Success(result.data as Products)
                response.value = ApiResult.Loading(false)
            }

            is NetworkResult.Error -> {
                response.value = ApiResult.Error(result.message ?: "")
                response.value = ApiResult.Loading(false)
            }
        }
    }
}