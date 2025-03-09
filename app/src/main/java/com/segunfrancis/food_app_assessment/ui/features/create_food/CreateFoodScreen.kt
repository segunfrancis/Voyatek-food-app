package com.segunfrancis.food_app_assessment.ui.features.create_food

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.segunfrancis.food_app_assessment.R
import com.segunfrancis.food_app_assessment.data.remote.Category
import com.segunfrancis.food_app_assessment.data.remote.Tag
import com.segunfrancis.food_app_assessment.ui.components.ImageComponent
import com.segunfrancis.food_app_assessment.ui.components.MenuComponent
import com.segunfrancis.food_app_assessment.ui.components.TagsComponent
import com.segunfrancis.food_app_assessment.ui.components.UploadComponent
import com.segunfrancis.food_app_assessment.ui.theme.Blue2
import com.segunfrancis.food_app_assessment.ui.theme.DefaultIconColor
import com.segunfrancis.food_app_assessment.ui.theme.VoyatekFoodAppTheme
import com.segunfrancis.food_app_assessment.ui.theme.White
import com.segunfrancis.food_app_assessment.util.createImageFile

@Composable
fun CreateFoodScreen(onBackPress: () -> Unit) {
    val viewModel = hiltViewModel<CreateFoodViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val buttonState by viewModel.buttonState.collectAsState()
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val uploadImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                viewModel.setImageUris(uris)
            } else {
                Toast.makeText(context, "No image was selected", Toast.LENGTH_LONG).show()
            }
        }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                photoUri?.let {
                    viewModel.setImageUris(listOf(it))
                }
            } else {
                Toast.makeText(context, "Failed to capture photo!", Toast.LENGTH_LONG).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.action.collect {
            when (it) {
                is CreateFoodAction.ShowMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }

                is CreateFoodAction.Success -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    onBackPress()
                }
            }
        }
    }

    if (uiState.isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(100.dp))
            }
        }
    }
    CreateFoodContent(buttonState = buttonState, uiState = uiState, onAction = {
        when (it) {
            CreateFoodScreenActions.OnBackClick -> {
                onBackPress()
            }

            is CreateFoodScreenActions.OnCaloriesChange -> {
                viewModel.setCalories(it.calories)
            }

            is CreateFoodScreenActions.OnCategorySelected -> {
                viewModel.setCategory(it.category)
            }

            is CreateFoodScreenActions.OnDescriptionChange -> {
                viewModel.setDescription(it.description)
            }

            is CreateFoodScreenActions.OnNameChange -> {
                viewModel.setName(it.name)
            }

            is CreateFoodScreenActions.OnTagSelected -> {
                viewModel.setTag(it.tag)
            }

            CreateFoodScreenActions.OnCreateClick -> {
                viewModel.onAddFoodClick()
            }

            CreateFoodScreenActions.OnTakePhotoClick -> {
                val file = createImageFile(context)
                photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                photoUri?.let { uri -> cameraLauncher.launch(uri) }
            }

            CreateFoodScreenActions.OnUploadClick -> {
                uploadImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            is CreateFoodScreenActions.OnDeleteTag -> {
                viewModel.deleteTag(it.tag)
            }

            is CreateFoodScreenActions.OnDeleteImage -> {
                viewModel.deleteUri(it.uri)
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFoodContent(
    buttonState: Boolean,
    uiState: CreateFoodUiState,
    onAction: (CreateFoodScreenActions) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = White)
    ) {
        val (toolbar, form, createButton) = createRefs()
        TopAppBar(
            title = {
                Text(
                    text = "Add new food",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            navigationIcon = {
                MenuComponent(
                    icon = R.drawable.ic_arrow_square_back,
                    onClick = { onAction(CreateFoodScreenActions.OnBackClick) },
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = White),
            modifier = Modifier.constrainAs(toolbar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .constrainAs(form) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(toolbar.bottom)
                bottom.linkTo(createButton.top)
                height = Dimension.fillToConstraints
            }) {
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UploadComponent(
                    image = R.drawable.ic_camera,
                    text = "Take Photo",
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 16.dp),
                    onClick = { onAction(CreateFoodScreenActions.OnTakePhotoClick) }
                )
                UploadComponent(
                    image = R.drawable.ic_upload,
                    text = "Upload",
                    modifier = Modifier
                        .weight(1F)
                        .padding(end = 16.dp),
                    onClick = { onAction(CreateFoodScreenActions.OnUploadClick) }
                )
            }
            Spacer(Modifier.height(12.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = uiState.imageUris, key = { it }) { uri ->
                    ImageComponent(modifier = Modifier.animateItem(), uri = uri, onDeleteClick = {
                        onAction(CreateFoodScreenActions.OnDeleteImage(uri))
                    })
                }
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Name",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { onAction(CreateFoodScreenActions.OnNameChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = "Enter food name",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${uiState.name.length}/30",
                            color = if (uiState.name.length > 30) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                isError = uiState.name.length > 30
            )

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Description",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { onAction(CreateFoodScreenActions.OnDescriptionChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = "Enter food description",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                minLines = 3,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Category",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            var expandedCategory by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = it }) {
                OutlinedTextField(
                    value = uiState.category?.name.orEmpty(),
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                    placeholder = {
                        Text(
                            text = "Select a category",
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_caret_down),
                            contentDescription = "Drop down icon"
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    readOnly = true
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }) {
                    uiState.categories.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                onAction(CreateFoodScreenActions.OnCategorySelected(it))
                                expandedCategory = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Calories",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.calories,
                onValueChange = { onAction(CreateFoodScreenActions.OnCaloriesChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = "Enter number of calories",
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Tags",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            var expandedTag by remember { mutableStateOf(false) }
            Spacer(Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = expandedTag,
                onExpandedChange = { expandedTag = it }) {
                TagsComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true),
                    tags = uiState.selectedTags.toList(),
                    onDelete = { onAction(CreateFoodScreenActions.OnDeleteTag(it)) }
                )
                ExposedDropdownMenu(
                    expanded = expandedTag,
                    onDismissRequest = { expandedTag = false }) {
                    uiState.tags.forEach { tag ->
                        DropdownMenuItem(text = {
                            Text(
                                text = tag.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }, onClick = {
                            expandedTag = false
                            onAction(CreateFoodScreenActions.OnTagSelected(tag))
                        })
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        ElevatedButton(
            onClick = { onAction(CreateFoodScreenActions.OnCreateClick) },
            shape = RoundedCornerShape(4.dp),
            enabled = buttonState,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Blue2
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp)
                .constrainAs(createButton) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, 24.dp)
                }) {
            Text(
                text = "Add food",
                color = if (buttonState) MaterialTheme.colorScheme.onPrimary else DefaultIconColor
            )
        }
    }
}

sealed class CreateFoodScreenActions {
    data class OnNameChange(val name: String) : CreateFoodScreenActions()
    data class OnDescriptionChange(val description: String) : CreateFoodScreenActions()
    data class OnCategorySelected(val category: Category) : CreateFoodScreenActions()
    data class OnCaloriesChange(val calories: String) : CreateFoodScreenActions()
    data class OnTagSelected(val tag: Tag) : CreateFoodScreenActions()
    data object OnBackClick : CreateFoodScreenActions()
    data object OnCreateClick : CreateFoodScreenActions()

    data object OnTakePhotoClick : CreateFoodScreenActions()

    data object OnUploadClick : CreateFoodScreenActions()

    data class OnDeleteTag(val tag: Tag) : CreateFoodScreenActions()

    data class OnDeleteImage(val uri: Uri) : CreateFoodScreenActions()
}

@Preview(showSystemUi = true)
@Composable
fun CreateFoodPreview() {
    VoyatekFoodAppTheme {
        CreateFoodContent(buttonState = false, uiState = CreateFoodUiState(), onAction = {})
    }
}
