package com.segunfrancis.food_app_assessment.ui.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.segunfrancis.food_app_assessment.R
import com.segunfrancis.food_app_assessment.ui.components.ChipGroup
import com.segunfrancis.food_app_assessment.ui.components.FoodComponent
import com.segunfrancis.food_app_assessment.ui.theme.VoyatekFoodAppTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    HomeContent(modifier = modifier, uiState = uiState)
}

@Composable
fun HomeContent(modifier: Modifier, uiState: HomeUiState) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(R.drawable.ic_user_avatar),
                contentDescription = "Profile photo",
                modifier = Modifier.size(42.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Hey there, Lucy!", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Are you excited to create a tasty dish today?",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(4.dp))
            TextField(
                value = "",
                onValueChange = {},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Search foods...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search icon"
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(Modifier.height(6.dp))
            ChipGroup(chipItems = uiState.categories, onItemClick = {})
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Text(text = "All Foods")
            }
            items(uiState.foods) { food ->
                FoodComponent(food = food)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    VoyatekFoodAppTheme {
        HomeContent(modifier = Modifier, uiState = HomeUiState())
    }
}
