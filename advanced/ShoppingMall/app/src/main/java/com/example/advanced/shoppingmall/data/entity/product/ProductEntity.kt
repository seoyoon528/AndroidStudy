package com.example.advanced.shoppingmall.data.entity.product

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 API를 통해 받아오는 데이터 타입(JSON)
 {
 "id": "1",
 "createdAt": "2022-03-22T19:44:11.1022",
 "product_name": "Handcrafted Fresh Keyboard",
 "product_price": "263.00",
 "product_image": "http://lorempixel.com/640/480/technics",
 "product": "Bike",
 "product_introduction_image": "Fantastic!"
 }
 */

@Entity
data class ProductEntity (

    @PrimaryKey val id: Long,
    val createdAt: Date,
    val productName: String,
    val productPrice: Int,
    val productImage: String,
    val productType: String,
    val productIntroductionImage: String

    )