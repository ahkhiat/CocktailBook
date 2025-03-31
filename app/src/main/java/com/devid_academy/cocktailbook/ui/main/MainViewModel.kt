package com.devid_academy.cocktailbook.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devid_academy.cocktailbook.data.api.ApiService
import com.devid_academy.cocktailbook.data.dto.DrinkLiteDTO
import com.devid_academy.cocktailbook.data.room.AppDatabase
import com.devid_academy.cocktailbook.data.room.model.DrinkDetailsRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _remoteDrinksListLiveData = MutableLiveData<List<DrinkLiteDTO>?>(emptyList())
    val remoteDrinksListLiveData: MutableLiveData<List<DrinkLiteDTO>?> = _remoteDrinksListLiveData

    private val _drinkDetailsRoomListLiveData = MutableLiveData<List<DrinkDetailsRoom>>(emptyList())
    val drinkDetailsRoomListLiveData : MutableLiveData<List<DrinkDetailsRoom>> = _drinkDetailsRoomListLiveData

    init {
        getRemoteDrinks()
    }

    fun getRemoteDrinks() {
        viewModelScope.launch {
            _isLoading.value = true
            val response = withContext(Dispatchers.IO) {
                apiService.getApi().getAllCocktails()
            }
            response?.let {
                if(response.isSuccessful) {
                    val result = response.body()

//                    val drinksWithIsMine = result?.drinksLiteDto?.map { drink ->
//                        drink.copy(isMine = false)
//                    }
//                    _remoteDrinksListLiveData.value = drinksWithIsMine

                    _remoteDrinksListLiveData.value = result?.drinksLiteDto

                    Log.i("MAIN VM", "Liste Retrofit Drinks Lite : " + result)
                } else when(response.code()){
                    403 -> {
                        Log.i("MAIN VM", "Erreur 403")
                    }
                    404 -> {
                        Log.i("MAIN VM", "Erreur 404")
                    }
                    500 -> {
                        Log.i("MAIN VM", "Erreur 500")
                    }
                    else -> {
                        Log.i("MAIN VM", "Erreur inconnue")
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun getAllDrinksDetailedRoom(){
        viewModelScope.launch {
            _drinkDetailsRoomListLiveData.value = emptyList<DrinkDetailsRoom>()

            val drinkList = withContext(Dispatchers.IO) {
                db.drinkDetailsDAO().getAll()
            }
            _drinkDetailsRoomListLiveData.value = drinkList.toList()
            Log.i("MAIN VM","Liste Room :" + drinkList.toString())
        }
    }

    fun insertDrinkDetailedRoom(drinkDetailedRoom : DrinkDetailsRoom){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.drinkDetailsDAO().insert(drinkDetailedRoom)
                Log.i("MAIN VM", "insert details")
            }
        }
    }

    fun deletedByIdDrinkDetailedRoom(id: Long){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.drinkDetailsDAO().deleteById(idDrink = id)
                Log.i("MAIN VM", "cocktail supprimé : id numero ${id}" )
            }
        }
    }

}