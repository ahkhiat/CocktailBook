package com.devid_academy.cocktailbook.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.cocktailbook.data.api.ApiService
import com.devid_academy.cocktailbook.data.dto.DrinkDetailsDTO
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
class DetailsViewModel @Inject constructor(
    private val apiService: ApiService
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _drinkLiveData = MutableLiveData<DrinkDetailsDTO>()
    val drinkLiveData: LiveData<DrinkDetailsDTO> = _drinkLiveData

    fun getRemoteDrink(drinkId: String) {
        viewModelScope.launch {
            Log.i("DETAILS VM", "Début de getRemoteDrinks()")
            _isLoading.postValue(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    Log.i("DETAILS VM", "Avant appel API")
                    apiService.getApi().getCocktail(drinkId.toInt())
                }
                Log.i("DETAILS VM", "Après appel API")

                response?.let {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _drinkLiveData.value = result?.drinksDetailsDto?.get(0)

                        Log.i("DETAILS VM", "Drink Details : " + _drinkLiveData.value)
                    } else {
                        Log.e("DETAILS VM", "Erreur HTTP ${response.code()}: ${response.message()}")
                    }
                }
            } catch (e: UnknownHostException) {
                Log.e("DETAILS VM", "Erreur réseau : Impossible de se connecter à l'API", e)
            } catch (e: SocketTimeoutException) {
                Log.e("DETAILS VM", "Erreur : Délai d'attente dépassé", e)
            } catch (e: Exception) {
                Log.e("DETAILS VM", "Erreur API : ${e.localizedMessage}", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }



}