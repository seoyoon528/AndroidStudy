package com.example.airbnb

data class HouseDto (       //  items로 HouseModel에 접근하기 위해 Dto 사용
    val items: List<HouseModel>
    )