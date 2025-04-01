package com.devid_academy.cocktailbook.data.room.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrinkLiteModel(

    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String,
    val isMine: Boolean = true

): Parcelable