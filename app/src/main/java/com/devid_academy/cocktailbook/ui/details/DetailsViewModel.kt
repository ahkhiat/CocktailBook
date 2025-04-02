package com.devid_academy.cocktailbook.ui.details

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
class DetailsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _drinkLiveData = MutableLiveData<DrinkDetailsDTO?>()
    val drinkLiveData: LiveData<DrinkDetailsDTO?> = _drinkLiveData

    private val _drinkRoomLiveData = MutableLiveData<DrinkDetailsRoom?>()
    val drinkRoomLiveData: LiveData<DrinkDetailsRoom?> = _drinkRoomLiveData

    private val _ingredientsLiveData = MutableLiveData<List<String>>()
    val ingredientsLiveData: LiveData<List<String>> get() = _ingredientsLiveData

    fun getIfRemoteOrRoom(drink: DrinkLiteModel) {
        Log.i("DETAILS VM ", "Get If remote or room " + drink.idDrink)
        if (drink.isMine) {
            getRoomDrink(drink.idDrink)
        } else {
            getRemoteDrink(drink.idDrink)

        }
    }

    fun getRoomDrink(drinkId: String) {
        viewModelScope.launch {
            Log.i("DETAILS VM", "Début de getRoomDrink()")
            try {
                _isLoading.value = true
                val result = withContext(Dispatchers.IO) {
                    db.drinkDetailsDAO().findByIdDrink(drinkId.toLong())
                }
                Log.i("DETAILS VM", "result : " + result)
                _drinkRoomLiveData.value = result
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("DETAILS VM", "Erreur Room : ${e.localizedMessage}", e)
            }
        }
    }

    fun getRemoteDrink(drinkId: String) {
        viewModelScope.launch {
            Log.i("DETAILS VM", "Début de getRemoteDrinks()")
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    Log.i("DETAILS VM", "Avant appel API")
                    apiService.getApi().getCocktail(drinkId.toInt())
                }
                Log.i("DETAILS VM", "Après appel API")

                response?.let {
                    if (response.isSuccessful) {
                        val result = response.body()
                        val drinkDetails = result?.drinksDetailsDto?.get(0)
                        _drinkLiveData.value = drinkDetails
                        drinkDetails?.let {
                            _ingredientsLiveData.value = getFormattedIngredients(it)
                        }
                        _isLoading.value = false
                        Log.i("DETAILS VM", "Drink Details : " + _drinkLiveData.value)
                    } else {
                        _isLoading.value = false
                        Log.e("DETAILS VM", "Erreur HTTP ${response.code()}: ${response.message()}")
                    }
                }
            } catch (e: UnknownHostException) {
                _isLoading.value = false
                Log.e("DETAILS VM", "Erreur réseau : Impossible de se connecter à l'API", e)
            } catch (e: SocketTimeoutException) {
                _isLoading.value = false
                Log.e("DETAILS VM", "Erreur : Délai d'attente dépassé", e)
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("DETAILS VM", "Erreur API : ${e.localizedMessage}", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun getFormattedIngredients(drink: DrinkDetailsDTO): List<String> {
        val ingredientsList = mutableListOf<String>()

        val ingredients = listOf(
            drink.strIngredient1 to drink.strMeasure1,
            drink.strIngredient2 to drink.strMeasure2,
            drink.strIngredient3 to drink.strMeasure3,
            drink.strIngredient4 to drink.strMeasure4,
            drink.strIngredient5 to drink.strMeasure5,
            drink.strIngredient6 to drink.strMeasure6,
            drink.strIngredient7 to drink.strMeasure7,
            drink.strIngredient8 to drink.strMeasure8,
            drink.strIngredient9 to drink.strMeasure9,
            drink.strIngredient10 to drink.strMeasure10,
            drink.strIngredient11 to drink.strMeasure11,
            drink.strIngredient12 to drink.strMeasure12,
            drink.strIngredient13 to drink.strMeasure13,
            drink.strIngredient14 to drink.strMeasure14,
            drink.strIngredient15 to drink.strMeasure15
        )
        ingredients.forEach { (ingredient, measure) ->
            if (!ingredient.isNullOrEmpty()) {
                ingredientsList.add("$ingredient (${measure ?: "N/A"})")
            }
        }
        return ingredientsList
    }



}