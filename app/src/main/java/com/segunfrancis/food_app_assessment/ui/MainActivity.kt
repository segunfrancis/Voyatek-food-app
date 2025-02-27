package com.segunfrancis.food_app_assessment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.segunfrancis.food_app_assessment.R
import com.segunfrancis.food_app_assessment.app_navigation.FoodAppBottomNav
import com.segunfrancis.food_app_assessment.app_navigation.NavDestinations
import com.segunfrancis.food_app_assessment.ui.features.add.AddScreen
import com.segunfrancis.food_app_assessment.ui.features.create_food.CreateFoodScreen
import com.segunfrancis.food_app_assessment.ui.features.favourite.FavouriteScreen
import com.segunfrancis.food_app_assessment.ui.features.generator.GeneratorScreen
import com.segunfrancis.food_app_assessment.ui.features.home.HomeScreen
import com.segunfrancis.food_app_assessment.ui.features.planner.PlannerScreen
import com.segunfrancis.food_app_assessment.ui.theme.Black1
import com.segunfrancis.food_app_assessment.ui.theme.VoyatekFoodAppTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            VoyatekFoodAppTheme {
                val currentBackStack by navController.currentBackStackEntryAsState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentBackStack?.destination?.hierarchy?.any {
                                it.hasRoute(NavDestinations.Home::class) || it.hasRoute(
                                    NavDestinations.Generator::class
                                ) || it.hasRoute(
                                    NavDestinations.Add::class
                                ) || it.hasRoute(
                                    NavDestinations.Favourite::class
                                ) || it.hasRoute(
                                    NavDestinations.Planner::class
                                )
                            } == true) {
                            FoodAppBottomNav(navController = navController)
                        }
                    },
                    floatingActionButton = {
                        if (currentBackStack?.destination?.hierarchy?.any {
                                it.hasRoute(NavDestinations.Home::class)
                            } == true) {
                            ExtendedFloatingActionButton(onClick = {
                                navController.navigate(NavDestinations.CreateFood)
                            }) {
                                Image(
                                    painter = painterResource(R.drawable.ic_add_food),
                                    contentDescription = "Add food icon"
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(text = "Add Food", style = MaterialTheme.typography.bodyMedium, color = Black1)
                            }
                        }
                    }) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = NavDestinations.Home
                    ) {
                        composable<NavDestinations.Home> {
                            HomeScreen(modifier = Modifier.padding(paddingValues))
                        }
                        composable<NavDestinations.Generator> {
                            GeneratorScreen(modifier = Modifier.padding(paddingValues))
                        }
                        composable<NavDestinations.Add> {
                            AddScreen(modifier = Modifier.padding(paddingValues))
                        }
                        composable<NavDestinations.Favourite> {
                            FavouriteScreen(modifier = Modifier.padding(paddingValues))
                        }
                        composable<NavDestinations.Planner> {
                            PlannerScreen(modifier = Modifier.padding(paddingValues))
                        }
                        composable<NavDestinations.CreateFood> {
                            CreateFoodScreen(onBackPress = { navController.navigateUp() } )
                        }
                    }
                }
            }
        }
    }
}
