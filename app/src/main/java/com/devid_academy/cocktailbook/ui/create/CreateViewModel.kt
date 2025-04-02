package com.devid_academy.cocktailbook.ui.create

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.cocktailbook.data.api.ApiService
import com.devid_academy.cocktailbook.data.room.AppDatabase
import com.devid_academy.cocktailbook.data.room.model.DrinkDetailsRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDrinkCreated = MutableLiveData<Boolean>(false)
    val isDrinkCreated: LiveData<Boolean> = _isDrinkCreated


    fun insertRoomDrink(drink: DrinkDetailsRoom) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                withContext(Dispatchers.IO) {
                    db.drinkDetailsDAO().insert(drink)
                }
                _isDrinkCreated.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("EDIT VM", "Erreur Insert Room : ${e.localizedMessage}", e)
            }

        }
    }

}