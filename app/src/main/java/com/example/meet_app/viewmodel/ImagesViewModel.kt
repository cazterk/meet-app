package com.example.meet_app.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor() : ViewModel() {
    fun saveProfileImageToFolder(context: Context, sourceUri: Uri): String? {
        val appFolder = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "Profile Photo"
        )
        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }
        val extension = MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(sourceUri))
        val filename = "profile_${System.currentTimeMillis()}.$extension"
        val destFile = File(appFolder, filename)

        try {
            context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
                FileOutputStream(destFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return destFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}