package com.example.intermediate.book_review.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.intermediate.book_review.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM  review WHERE id == :id")
    fun getOneReview(id: Int): Review

    @Insert(onConflict = OnConflictStrategy.REPLACE)        // 중복될 경우 새로운 것으로 대체
    fun saveReview(review: Review)

}