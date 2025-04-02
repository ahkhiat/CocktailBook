package com.devid_academy.cocktailbook.ui.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.cocktailbook.data.api.ApiService
import com.devid_academy.cocktailbook.data.dto.DrinkDetailsDTO
import com.devid_academy.cocktailbook.data.room.AppDatabase
import com.devid_academy.cocktailbook.data.room.model.DrinkDetailsRoom
import com.devid_academy.cocktailbook.data.room.model.DrinkLiteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _drinkRoomLiveData = MutableLiveData<DrinkDetailsRoom?>()
    val drinkRoomLiveData: LiveData<DrinkDetailsRoom?> = _drinkRoomLiveData

    private val _isDrinkUpdated = MutableLiveData<Boolean>(false)
    val isDrinkUpdated: LiveData<Boolean> = _isDrinkUpdated

    fun getRoomDrink(drinkId: Long) {
        viewModelScope.launch {
            Log.i("DETAILS VM", "Début de getRoomDrink()")
            try {
                _isLoading.value = true
                val result = withContext(Dispatchers.IO) {
                    db.drinkDetailsDAO().findByIdDrink(drinkId)
                }
                Log.i("DETAILS VM", "result : " + result)
                _drinkRoomLiveData.value = result
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("DETAILS VM", "Erreur Get Room : ${e.localizedMessage}", e)
            }
        }
    }

    fun editRoomDrink(drink: DrinkDetailsRoom) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                withContext(Dispatchers.IO) {
                    db.drinkDetailsDAO().update(drink)
                }
                _isDrinkUpdated.value = true
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("EDIT VM", "Erreur Insert Room : ${e.localizedMessage}", e)
            }

        }
    }




}