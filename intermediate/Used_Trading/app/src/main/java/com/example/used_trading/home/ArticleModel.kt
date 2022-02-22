package com.example.used_trading.home

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
) {
        //  Firebase Realtime database에 Model class를 그대로 사용하기 위해서는 빈 생성자 필요
        constructor(): this("", "", 0, "", "")

}
