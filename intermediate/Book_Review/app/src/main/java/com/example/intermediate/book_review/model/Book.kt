package com.example.intermediate.book_review.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize      // data class 직렬화
data class Book (
        @SerializedName("itemId") val id : Long,     // 서버의 itemId와 Book data class의 id를 맵핑
        @SerializedName("title") val title : String,
        @SerializedName("description") val description : String,
        @SerializedName("coverSmallUrl") val coverSmallUrl : String,
        @SerializedName("coverLargeUrl") val coverLargeUrl : String
) : Parcelable