package com.example.intermediate.book_review.api

import com.example.intermediate.book_review.model.BestSellerDto
import com.example.intermediate.book_review.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/api/search.api?output=json")     // 책 검색 API
    fun getBookByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")      // 베스트셀러 API
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>
}