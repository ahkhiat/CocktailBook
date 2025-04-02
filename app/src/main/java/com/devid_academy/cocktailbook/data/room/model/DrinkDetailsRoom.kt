package com.devid_academy.cocktailbook.data.room.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "drink_details_room")
@Parcelize
data class DrinkDetailsRoom(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idDrink")
    val idDrink: Long,

    @ColumnInfo(name = "strDrink")
    val strDrink: String,

    @ColumnInfo(name = "strDrinkThumb")
    val strDrinkThumb: String,

    @ColumnInfo(name = "strIngredients")
    val strIngredients: String,

    @ColumnInfo(name = "strInstructions")
    val strInstructions: String,

    @ColumnInfo(name = "isMine")
    val isMine: Boolean = true
) : Parcelable