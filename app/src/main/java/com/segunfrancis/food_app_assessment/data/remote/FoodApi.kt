package com.segunfrancis.food_app_assessment.data.remote

import retrofit2.http.GET

interface FoodApi {

    @GET("api/foods")
    suspend fun getFoods(): BaseResponse<List<Food>>

    @GET("api/categories")
    suspend fun getCategories(): BaseResponse<List<Category>>
}
