package com.segunfrancis.food_app_assessment.ui.features.favourite

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavouriteScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Favourite Screen",
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.padding(24.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
