package com.example.sokodata.util

import android.Manifest
import android.os.Build
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

/**
 * Utilitaire pour gérer les permissions et accéder à la caméra/galerie
 */

@Composable
fun rememberPhotoPickerLauncher(
    onPhotoSelected: (ByteArray?) -> Unit
): android.app.Activity? {
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    var galleryPermissionGranted by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Launcher pour caméra
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // La photo a été capturée
            onPhotoSelected(null) // TODO: Récupérer les bytes de la photo
        }
    }

    // Launcher pour galerie
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // La photo a été sélectionnée
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.use {
                    val bytes = it.readBytes()
                    onPhotoSelected(bytes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onPhotoSelected(null)
            }
        }
    }

    // Launcher pour permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        cameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: false
        galleryPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
    }

    LaunchedEffect(Unit) {
        val permissionsNeeded = mutableListOf(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            permissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES)
        }
        permissionLauncher.launch(permissionsNeeded.toTypedArray())
    }

    return null
}

@Composable
fun CameraPhotoCapture(
    onPhotoSelected: (ByteArray?) -> Unit
) {
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            // Convertir le bitmap en bytes
            val outputStream = java.io.ByteArrayOutputStream()
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, outputStream)
            val bytes = outputStream.toByteArray()
            onPhotoSelected(bytes)
        } else {
            onPhotoSelected(null)
        }
    }

    fun openCamera() {
        try {
            cameraLauncher.launch(null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun GalleryPhotoPicker(
    onPhotoSelected: (ByteArray?) -> Unit
) {
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.use {
                    val bytes = it.readBytes()
                    onPhotoSelected(bytes)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onPhotoSelected(null)
            }
        }
    }

    fun openGallery() {
        galleryLauncher.launch("image/*")
    }
}
