package com.segunfrancis.food_app_assessment.ui.features.home

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.segunfrancis.food_app_assessment.R
import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.ui.components.ChipGroup
import com.segunfrancis.food_app_assessment.ui.components.FoodComponent
import com.segunfrancis.food_app_assessment.ui.components.MenuComponent
import com.segunfrancis.food_app_assessment.ui.theme.Black1
import com.segunfrancis.food_app_assessment.ui.theme.Grey3
import com.segunfrancis.food_app_assessment.ui.theme.Grey4
import com.segunfrancis.food_app_assessment.ui.theme.VoyatekFoodAppTheme
import com.segunfrancis.food_app_assessment.ui.theme.White

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                is HomeAction.ShowMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val pullToRefreshState = remember {
        object : PullToRefreshState {
            private val anim = androidx.compose.animation.core.Animatable(0f, Float.VectorConverter)
            override val distanceFraction: Float
                get() = anim.value

            override val isAnimating: Boolean
                get() = anim.isRunning

            override suspend fun animateToThreshold() {
                anim.animateTo(1F, spring(dampingRatio = Spring.DampingRatioHighBouncy))
            }

            override suspend fun animateToHidden() {
                anim.animateTo(0F)
            }

            override suspend fun snapTo(targetValue: Float) {
                anim.snapTo(targetValue)
            }
        }
    }

    HomeContent(modifier = modifier, uiState = uiState, onCategoryClicked = {
        viewModel.setCategory(it)
    }, state = pullToRefreshState, onRefresh = { viewModel.refresh() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier,
    uiState: HomeUiState,
    onCategoryClicked: (Category) -> Unit,
    state: PullToRefreshState,
    onRefresh: () -> Unit
) {
    val config = LocalConfiguration.current

    Column(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.background(color = White)) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_user_avatar),
                    contentDescription = "Profile photo",
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.CenterStart)
                )
                MenuComponent(
                    icon = R.drawable.ic_notification, onClick = {}, modifier = Modifier.align(
                        Alignment.CenterEnd
                    )
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Hey there, Lucy!",
                style = MaterialTheme.typography.titleMedium,
                color = Black1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Are you excited to create a tasty dish today?",
                style = MaterialTheme.typography.bodySmall,
                color = Grey3,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            TextField(
                value = "",
                onValueChange = {},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                label = {
                    Text(
                        text = "Search foods...",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = "Search icon"
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Grey4,
                    unfocusedContainerColor = Grey4
                )
            )
            Spacer(Modifier.height(6.dp))
            if (uiState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            ChipGroup(chipItems = uiState.categories, onItemClick = {
                onCategoryClicked(it)
            })
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(8.dp))
        if (config.screenWidthDp > 600) {
            if (uiState.foods.isNotEmpty()) {
                Text(
                    text = uiState.currentCategory?.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Black1
                )
            }
            PullToRefreshBox(
                state = state,
                isRefreshing = uiState.isLoading,
                onRefresh = { onRefresh() }) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(items = uiState.foods, key = { it.id }) { food ->
                        FoodComponent(food = food, modifier = Modifier.animateItem())
                    }
                }
            }
        } else {
            PullToRefreshBox(
                state = state,
                isRefreshing = uiState.isLoading,
                onRefresh = { onRefresh() }) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = White),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    item {
                        if (uiState.foods.isNotEmpty()) {
                            Text(
                                text = uiState.currentCategory?.name.orEmpty(),
                                style = MaterialTheme.typography.titleMedium,
                                color = Black1
                            )
                        }
                    }
                    items(items = uiState.foods, key = { it.id }) { food ->
                        FoodComponent(food = food, modifier = Modifier.animateItem())
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    VoyatekFoodAppTheme {
        HomeContent(
            modifier = Modifier,
            uiState = HomeUiState(),
            onCategoryClicked = {},
            state = rememberPullToRefreshState(),
            onRefresh = {}
        )
    }
}
