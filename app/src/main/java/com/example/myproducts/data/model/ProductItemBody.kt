package com.example.myproducts.data.model

import java.io.File

data class ProductItemBody(
    val product_name: String,
    val product_type: String,
    val price: String,
    val tax: String,
    val images: List<File> = emptyList(),
)