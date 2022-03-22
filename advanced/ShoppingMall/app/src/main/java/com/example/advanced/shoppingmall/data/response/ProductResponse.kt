package com.example.advanced.shoppingmall.data.response

import com.example.advanced.shoppingmall.data.entity.product.ProductEntity
import java.util.*

data class ProductResponse (
    val id: String,
    val createdAt: Long,
    val productName: String,
    val productPrice: String,
    val productImage: String,
    val productType: String,
    val productIntroductionImage: String
) {

    // Entity로 바꿔주는 function
    fun toEntity(): ProductEntity =
        ProductEntity(
            id = id.toLong(),
            createdAt = Date(createdAt),
            productName = productName,
            productPrice = productPrice.toDouble().toInt(),
            productImage = productImage,
            productType = productType,
            productIntroductionImage = productIntroductionImage
        )

}