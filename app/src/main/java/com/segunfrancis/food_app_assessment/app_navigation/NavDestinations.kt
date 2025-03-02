package com.segunfrancis.food_app_assessment.app_navigation

import com.segunfrancis.food_app_assessment.data.remote.Food
import kotlinx.serialization.Serializable

sealed class NavDestinations {
    @Serializable
    data object Home : NavDestinations()

    @Serializable
    data object Generator : NavDestinations()

    @Serializable
    data object Add : NavDestinations()

    @Serializable
    data object Favourite : NavDestinations()

    @Serializable
    data object Planner : NavDestinations()

    @Serializable
    data object CreateFood : NavDestinations()

    @Serializable
    data class FoodDetails(val food: Food) : NavDestinations()
}
