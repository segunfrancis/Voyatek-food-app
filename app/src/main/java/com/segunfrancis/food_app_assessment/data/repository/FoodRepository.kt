package com.segunfrancis.food_app_assessment.data.repository

import com.segunfrancis.food_app_assessment.data.remote.BaseResponse
import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.data.remote.Food
import com.segunfrancis.food_app_assessment.data.remote.Tag
import com.segunfrancis.food_app_assessment.ui.features.create_food.CreateFood

interface FoodRepository {

    suspend fun getFoods(): Result<List<Food>>

    suspend fun getCategories(): Result<List<Category>>

    suspend fun createFood(food: CreateFood): Result<BaseResponse<Food>>

    fun getLocalCategories(): List<Category>

    suspend fun getTags(): Result<List<Tag>>
}
