package com.example.advanced.shoppingmall.domain

import com.example.advanced.shoppingmall.data.entity.product.ProductEntity
import com.example.advanced.shoppingmall.data.repository.ProductRepository

internal class OrderProductItemUseCase (
    private val productRepository: ProductRepository          // ProductRepository를 주입받아서
): UseCase {

    suspend operator fun invoke(productEntity: ProductEntity): Long {          // 호출 시점에 ProductRepository를 호출
        return productRepository.insertProductItem(productEntity)
    }

}
