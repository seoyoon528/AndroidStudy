package com.example.advanced.shoppingmall.data.response

data class ProductsResponse (
    val items: List<ProductResponse>,
    val count: Int
)