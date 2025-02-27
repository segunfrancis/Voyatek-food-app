package com.segunfrancis.food_app_assessment.util

import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getMediaTypeForFile(file: File): MediaType? {
    return when (file.extension.lowercase()) {
        "png" -> "image/png".toMediaTypeOrNull()
        "jpg", "jpeg" -> "image/jpeg".toMediaTypeOrNull()
        "webp" -> "image/webp".toMediaTypeOrNull()
        else -> "application/octet-stream".toMediaTypeOrNull()
    }
}
 fun uriToFile(context: Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "temp_image")
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}

fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(null)
    return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
}
