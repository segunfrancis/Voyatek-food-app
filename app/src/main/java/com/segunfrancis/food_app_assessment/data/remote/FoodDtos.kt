package com.segunfrancis.food_app_assessment.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Food(
    val calories: Int,
    val category: Category,
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("created_at")
    val createdAt: String,
    val description: String,
    val foodImages: List<FoodImage>,
    val foodTags: List<String>,
    val id: Int,
    val name: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class Category(
    @SerialName("created_at")
    val createdAt: String,
    val description: String,
    val id: Int,
    val name: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class FoodImage(
    val id: Int,
    @SerialName("image_url")
    val imageUrl: String
)

@Serializable
data class BaseResponse<T>(@SerialName("data") val data: T, val message: String, val status: String)
