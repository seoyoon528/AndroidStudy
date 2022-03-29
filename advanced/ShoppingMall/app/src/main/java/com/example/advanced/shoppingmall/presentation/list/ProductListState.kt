package com.example.advanced.shoppingmall.presentation.list

import com.example.advanced.shoppingmall.data.entity.product.ProductEntity

sealed class ProductListState {

    object UnInitialized: ProductListState()            // UI 초기화

    object Loading: ProductListState()

    data class Success(
        val productList: List<ProductEntity>
    ): ProductListState()

    object Error: ProductListState()
}