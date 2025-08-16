package com.example.contactapp

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun copyUriToInternalStorage(context: Context, uri: Uri, fileName: String): String? {
    val file = File(context.filesDir, fileName)
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}