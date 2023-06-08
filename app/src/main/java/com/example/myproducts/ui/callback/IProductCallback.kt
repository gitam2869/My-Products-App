package com.example.myproducts.ui.callback

import com.example.runningtrackerapp.data.model.ProductItem

interface IProductCallback {
    fun onClick(position: Int, productItem: ProductItem)
}