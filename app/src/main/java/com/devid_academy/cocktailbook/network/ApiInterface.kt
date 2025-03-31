package com.devid_academy.cocktailbook.data.api

import com.devid_academy.cocktailbook.data.dto.DrinkLiteDTO
import com.devid_academy.cocktailbook.data.dto.ResponseAllDrinksLite
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET(ApiRoutes.ALL_COCKTAILS)
    suspend fun getAllCocktails(): Response<ResponseAllDrinksLite>?

}