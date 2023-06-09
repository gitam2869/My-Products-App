package com.example.myproducts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproducts.data.model.AddProductResponse
import com.example.myproducts.data.model.ProductItemBody
import com.example.myproducts.data.repository.ProductRepository
import com.example.myproducts.utils.ApiResult
import com.example.myproducts.utils.NetworkResult

class AddProductViewModel constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    var response: MutableLiveData<ApiResult<AddProductResponse>> = MutableLiveData()

    suspend fun addProduct(productItemBody: ProductItemBody) {
        response.value = ApiResult.Loading(true)
        when (val result = productRepository.addProduct(productItemBody)) {
            is NetworkResult.Success -> {
                response.value = ApiResult.Success(result.data as AddProductResponse)
                response.value = ApiResult.Loading(false)
            }

            is NetworkResult.Error -> {
                response.value = ApiResult.Error(result.message ?: "")
                response.value = ApiResult.Loading(false)
            }
        }
    }
}