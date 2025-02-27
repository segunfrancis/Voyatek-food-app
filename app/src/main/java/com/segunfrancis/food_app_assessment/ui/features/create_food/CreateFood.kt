package com.segunfrancis.food_app_assessment.ui.features.create_food

import android.net.Uri

data class CreateFood(
    val name: String,
    val description: String,
    val categoryId: Int,
    val calories: Int,
    val tags: List<String>,
    val images: List<Uri>
)
