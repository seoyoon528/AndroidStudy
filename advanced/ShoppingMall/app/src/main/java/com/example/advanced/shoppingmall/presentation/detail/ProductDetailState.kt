package com.example.advanced.shoppingmall.presentation.detail

import com.example.advanced.shoppingmall.data.entity.product.ProductEntity

sealed class ProductDetailState {

    object UnInitialized: ProductDetailState()            // UI 초기화

    object Loading: ProductDetailState()

    data class Success(
        val productEntity: ProductEntity
    ): ProductDetailState()

    object Order: ProductDetailState()          //  주문이 완료되면 화면 종료

    object Error: ProductDetailState()
}