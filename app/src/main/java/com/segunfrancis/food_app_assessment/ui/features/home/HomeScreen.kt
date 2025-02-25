package com.segunfrancis.food_app_assessment.ui.features.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Home Screen",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(24.dp)
    )
}
