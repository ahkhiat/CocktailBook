package com.devid_academy.cocktailbook.data.room.model


data class DrinkLiteModel(

    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val isMine: Boolean = true

)