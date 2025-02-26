package com.segunfrancis.food_app_assessment.data.repository

import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.data.remote.Food

interface FoodRepository {

    suspend fun getFoods(): Result<List<Food>>

    suspend fun getCategories(): Result<List<Category>>
}
