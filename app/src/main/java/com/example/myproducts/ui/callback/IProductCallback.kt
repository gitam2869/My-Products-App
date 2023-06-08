package com.example.myproducts.ui.callback

import com.example.myproducts.data.model.ProductDetails

interface IProductCallback {
    fun onClick(position: Int, productItem: ProductDetails)
}