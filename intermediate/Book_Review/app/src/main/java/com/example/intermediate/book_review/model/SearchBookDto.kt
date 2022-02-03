package com.example.intermediate.book_review.model

import com.google.gson.annotations.SerializedName

data class SearchBookDto(       // DTO :: 전체 모델에서 데이터를 꺼내올 수 있게 연결시켜주는 역할
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)
