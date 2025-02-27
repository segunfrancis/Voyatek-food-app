package com.segunfrancis.food_app_assessment.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.segunfrancis.food_app_assessment.R
import com.segunfrancis.food_app_assessment.ui.theme.VoyatekFoodAppTheme

@Composable
fun ImageComponent(uri: Uri?, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(50.dp)
            .clip(shape = RoundedCornerShape(4.dp))
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "uploaded_image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Image(
            painter = painterResource(R.drawable.ic_delete_image),
            contentDescription = "delete image",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onDeleteClick() }
                .align(Alignment.BottomEnd)
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ImageComponentPreview() {
    VoyatekFoodAppTheme {
        ImageComponent(null, onDeleteClick = {})
    }
}
