package com.devid_academy.cocktailbook.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devid_academy.cocktailbook.data.room.dao.DrinkDetailsDAO
import com.devid_academy.cocktailbook.data.room.model.DrinkDetailsRoom

@Database(entities = [DrinkDetailsRoom::class], version = 1, exportSchema = false)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun drinkDetailsDAO(): DrinkDetailsDAO
}

