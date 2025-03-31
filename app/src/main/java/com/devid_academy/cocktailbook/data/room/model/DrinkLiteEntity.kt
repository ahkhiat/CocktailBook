package com.devid_academy.cocktailbook.data.room.model


import androidx.room.Entity


@Entity(tableName = "drink_entity")

data class DrinkLiteEntity(

    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val isMine: Boolean

)