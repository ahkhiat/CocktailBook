package com.devid_academy.cocktailbook.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.devid_academy.cocktailbook.data.api.ApiService
import com.devid_academy.cocktailbook.data.dto.DrinkLiteDTO
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
class MainViewModel @Inject constructor(
    private val apiService: ApiService,
    private val db: AppDatabase
): ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _remoteDrinksListLiveData = MutableLiveData<List<DrinkLiteModel>?>(emptyList())
    val remoteDrinksListLiveData: MutableLiveData<List<DrinkLiteModel>?> = _remoteDrinksListLiveData

    private val _roomDrinksListLiveData = MutableLiveData<List<DrinkLiteModel>>(emptyList())
    val roomDrinksListLiveData: MutableLiveData<List<DrinkLiteModel>> =
        _roomDrinksListLiveData

    private val _combinedDrinksListLiveData = MediatorLiveData<List<DrinkLiteModel>>()
    val combinedDrinksListLiveData: LiveData<List<DrinkLiteModel>> = _combinedDrinksListLiveData


    init {
        Log.i("MAIN VM", "MainViewModel instancié")
        getRemoteDrinks()
        getAllDrinksDetailedRoom()

//        insertDrinkDetailedRoom(DrinkDetailsRoom(
//            0,
//            "PastisPiscine",
//            "urlimage2",
//            "2cl de get",
//            "ajouter glacon"
//        ))
//        insertDrinkDetailedRoom(DrinkDetailsRoom(
//            0,
//            "vodka redbull",
//            "urlimage3",
//            "2cl de vodka",
//            "blabla"
//        ))

        _combinedDrinksListLiveData.addSource(roomDrinksListLiveData) { result ->
            _combinedDrinksListLiveData.value = combineDrinks(result ?: emptyList(), remoteDrinksListLiveData.value ?: emptyList())
        }

        _combinedDrinksListLiveData.addSource(remoteDrinksListLiveData) { result ->
            _combinedDrinksListLiveData.value = combineDrinks(result ?: emptyList(), roomDrinksListLiveData.value ?: emptyList())
        }
    }

    fun getRoomListSize(): Int {
        return roomDrinksListLiveData.value?.size ?: 0
    }

    fun getRemoteListSize(): Int {
        return remoteDrinksListLiveData.value?.size ?: 0
    }

    fun getCombinedListSize(): Int {
        return _combinedDrinksListLiveData.value?.size ?: 0
    }

    fun combineDrinks(
        roomList: List<DrinkLiteModel>,
        remoteList: List<DrinkLiteModel>
    ): List<DrinkLiteModel> {
        return (roomList + remoteList).distinctBy {
            it.idDrink
        }
    }

    fun getRemoteDrinks() {
        viewModelScope.launch {
            Log.i("MAIN VM", "Début de getRemoteDrinks()")
            _isLoading.postValue(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    Log.i("MAIN VM", "Avant appel API")
                    apiService.getApi().getAllCocktails()
                }
                Log.i("MAIN VM", "Après appel API")

                response?.let {
                    if (response.isSuccessful) {
                        val result = response.body()
                        _remoteDrinksListLiveData.value = (result?.drinksLiteDto)?.map{
                            DrinkLiteModel(
                                idDrink = it.idDrink,
                                strDrink = it.strDrink,
                                strDrinkThumb = it.strDrinkThumb,
                                isMine = it.isMine
                            )
                        }
                        Log.i("MAIN VM", "Liste Retrofit Drinks Lite : " + _remoteDrinksListLiveData.value)
                    } else {
                        Log.e("MAIN VM", "Erreur HTTP ${response.code()}: ${response.message()}")
                    }
                }
            } catch (e: UnknownHostException) {
                Log.e("MAIN VM", "Erreur réseau : Impossible de se connecter à l'API", e)
            } catch (e: SocketTimeoutException) {
                Log.e("MAIN VM", "Erreur : Délai d'attente dépassé", e)
            } catch (e: Exception) {
                Log.e("MAIN VM", "Erreur API : ${e.localizedMessage}", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun getAllDrinksDetailedRoom(){
        viewModelScope.launch {
            try {
                val drinkList = withContext(Dispatchers.IO) {
                    db.drinkDetailsDAO().getAll()
                }
                _roomDrinksListLiveData.value = drinkList.map {
                    DrinkLiteModel(
                        idDrink = it.idDrink.toString(),
                        strDrink = it.strDrink,
                        strDrinkThumb = it.strDrinkThumb,
                        isMine = it.isMine)
                }
            } catch (e: Exception) {
                Log.e("MAIN VM", "GetAll Room Erreur :" + e.localizedMessage)
            }
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