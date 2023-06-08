package com.example.myproducts.data.repository

import android.util.Log
import com.example.myproducts.data.api.ApiServices
import com.example.myproducts.data.model.ProductItemBody
import com.example.myproducts.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class ProductRepository constructor(private val apiServices: ApiServices) {

    private val TAG = "ProductRepository"

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

    suspend fun addProduct(productItemBody: ProductItemBody): NetworkResult<*> {
        return try {
            val response =
                apiServices.addProduct(getDataBody(productItemBody), getImages(productItemBody))
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            Log.d(TAG, "addProduct: 1 "+e.message())
            NetworkResult.Error(e.message(), null)
        } catch (e: IOException) {
            Log.d(TAG, "addProduct: 2 "+e.message)
            NetworkResult.Error(e.message ?: "", null)
        } catch (e: Exception) {
            Log.d(TAG, "addProduct: 3 "+e.message)
            NetworkResult.Error(e.message ?: "", null)
        }
    }

    private fun getDataBody(productItemBody: ProductItemBody): HashMap<String, RequestBody> {
        val nameBody = productItemBody.product_name.toRequestBody(MultipartBody.FORM)
        val typeBody = productItemBody.product_type.toRequestBody(MultipartBody.FORM)
        val priceBody = productItemBody.price.toRequestBody(MultipartBody.FORM)
        val taxBody = productItemBody.tax.toRequestBody(MultipartBody.FORM)

        val dataBody = HashMap<String, RequestBody>()
        dataBody["product_name"] = nameBody
        dataBody["product_type"] = typeBody
        dataBody["price"] = priceBody
        dataBody["tax"] = taxBody

        return dataBody
    }


    private fun getImage(imageFile: File): MultipartBody.Part {
        val profileImage: RequestBody =
            imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        return profileImage.let {
            MultipartBody.Part.createFormData(
                "files[]",
                imageFile.name,
                it
            )
        }
    }

    private fun getImages(productItemBody: ProductItemBody): MutableList<MultipartBody.Part> {
        val files: MutableList<MultipartBody.Part> = mutableListOf()

        for (imageFile in productItemBody.images) {
            val profileImage: RequestBody =
                imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val profileImageBody: MultipartBody.Part =
                profileImage.let {
                    MultipartBody.Part.createFormData(
                        "files[]",
                        imageFile.name,
                        it
                    )
                }

            files.add(profileImageBody)
        }

        return files
    }
}