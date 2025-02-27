package com.segunfrancis.food_app_assessment.app_navigation

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
}
