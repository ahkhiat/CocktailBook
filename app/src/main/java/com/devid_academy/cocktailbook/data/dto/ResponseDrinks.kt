package com.devid_academy.cocktailbook.data.dto

import com.squareup.moshi.Json

data class ResponseDrinkDetailsDTO(
    @Json(name = "drinks")
    val drinksDetailsDto: List<DrinkDetailsDTO>
)

data class ResponseAllDrinksLite(
    @Json(name = "drinks")
    val drinksLiteDto: List<DrinkLiteDTO>
)