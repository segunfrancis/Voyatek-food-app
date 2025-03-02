package com.segunfrancis.food_app_assessment.data.repository

import android.content.Context
import com.segunfrancis.food_app_assessment.data.IODispatcher
import com.segunfrancis.food_app_assessment.data.local.VoyatekAppDatabase
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
    private val api: FoodApi,
    private val database: VoyatekAppDatabase,
    @IODispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : FoodRepository {

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
        var categories: List<Category> = emptyList()
        return try {
            categories = database.getCategories().ifEmpty {
                withContext(dispatcher) { api.getCategories().data }
            }
            Result.success(categories)
        } catch (t: Throwable) {
            Result.failure(t)
        } finally {
            database.setCategories(categories)
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
                        name = "images[$index]",
                        filename = "${imageFile.name}_$index",
                        body = imageFile.asRequestBody(getMediaTypeForFile(imageFile))
                    )
                }
                val response = api.createFood(name, description, categoryId, calories, tags, images)
                Result.success(response)
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    override suspend fun getTags(): Result<List<Tag>> {
        var tags: List<Tag> = emptyList()
        return try {
            tags = database.getTags().ifEmpty {
                withContext(dispatcher) {
                    api.getTags().data
                }
            }
            Result.success(tags)
        } catch (t: Throwable) {
            Result.failure(t)
        } finally {
            database.setTags(tags)
        }
    }
}
