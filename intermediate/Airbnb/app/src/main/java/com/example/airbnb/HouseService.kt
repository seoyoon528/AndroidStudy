package com.example.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/ff3f8991-260a-4e4f-a9e1-4431b5d9bcab")        //  Mock API
    fun getHouseList(): Call<HouseDto>
}