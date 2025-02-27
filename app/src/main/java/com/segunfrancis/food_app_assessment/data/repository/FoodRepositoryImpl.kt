package com.segunfrancis.food_app_assessment.data.repository

import android.content.Context
import com.segunfrancis.food_app_assessment.data.IODispatcher
import com.segunfrancis.food_app_assessment.data.remote.BaseResponse
import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.data.remote.Food
import com.segunfrancis.food_app_assessment.data.remote.FoodApi
import com.segunfrancis.food_app_assessment.data.remote.Tag
import com.segunfrancis.food_app_assessment.ui.features.create_food.CreateFood
import com.segunfrancis.food_app_assessment.util.getMediaTypeForFile
import com.segunfrancis.food_app_assessment.util.uriToFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    private val api: FoodApi,
    @ApplicationContext private val context: Context
) : FoodRepository {

    /**
     * This is used to simulate a local storage
     **/
    private val categories = mutableListOf<Category>()

    override suspend fun getFoods(): Result<List<Food>> {
        return try {
            val response = withContext(dispatcher) {
                api.getFoods().data.sortedByDescending { it.createdAt }
            }
            Result.success(response)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val response = withContext(dispatcher) { api.getCategories().data }
            categories.addAll(response)
            Result.success(response)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun createFood(food: CreateFood): Result<BaseResponse<Food>> {
        return try {
            withContext(dispatcher) {
                val name = food.name.toRequestBody("text/plain".toMediaType())
                val description = food.description.toRequestBody("text/plain".toMediaType())
                val categoryId =
                    food.categoryId.toString().toRequestBody("text/plain".toMediaType())
                val calories = food.calories.toString().toRequestBody("text/plain".toMediaType())
                val tags = food.tags.mapIndexed { index, tag ->
                    val tagRequestBody = tag.toRequestBody("text/plain".toMediaType())
                    MultipartBody.Part.createFormData("tags[$index]", null, tagRequestBody)
                }

                val images = food.images.mapIndexed { index, uri ->
                    val imageFile = uriToFile(context, uri)
                    MultipartBody.Part.createFormData(
                        "images[$index]",
                        imageFile.name,
                        imageFile.asRequestBody(getMediaTypeForFile(imageFile))
                    )
                }
                val response = api.createFood(name, description, categoryId, calories, tags, images)
                Result.success(response)
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override fun getLocalCategories(): List<Category> {
        return categories
    }

    override suspend fun getTags(): Result<List<Tag>> {
        return try {
            val response = withContext(dispatcher) {
                api.getTags().data
            }
            Result.success(response)
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }
}
