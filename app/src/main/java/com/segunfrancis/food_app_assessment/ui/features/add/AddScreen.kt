package com.segunfrancis.food_app_assessment.ui.features.add

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.segunfrancis.food_app_assessment.ui.theme.VoyatekFoodAppTheme

@Composable
fun AddScreen(modifier: Modifier = Modifier) {
    Text(
        text = "Add Screen",
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(24.dp)
    )
}


@Composable
@Preview(showSystemUi = true)
fun AddScreenPreview() {
    VoyatekFoodAppTheme {
        Surface {
            AddScreen()
        }
    }
}
