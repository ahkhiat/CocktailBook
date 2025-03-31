package com.devid_academy.cocktailbook.data.dto
import com.squareup.moshi.Json

data class DrinkLiteDTO(

//    @Json(name = "idDrink")
    val idDrink: String,
//    @Json(name = "strDrink")
    val strDrink: String,
//    @Json(name = "strDrinkThumb")
    val strDrinkThumb: String,

    val isMine: Boolean = false
)