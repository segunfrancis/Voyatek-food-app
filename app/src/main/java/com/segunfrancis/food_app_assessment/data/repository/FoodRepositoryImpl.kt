package com.segunfrancis.food_app_assessment.data.repository

import com.segunfrancis.food_app_assessment.data.IODispatcher
import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.data.remote.Food
import com.segunfrancis.food_app_assessment.data.remote.FoodApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val api: FoodApi
) :
    FoodRepository {
    override suspend fun getFoods(): Result<List<Food>> {
        return try {
            val response = withContext(dispatcher) {
                api.getFoods().data
            }
            Result.success(response)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = withContext(dispatcher) { api.getCategories().data }
            Result.success(response)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}
