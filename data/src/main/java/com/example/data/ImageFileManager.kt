package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.UUID
import javax.inject.Inject

class ImageFileManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient
) {
    private val imagesDir: File = context.filesDir
    private val placeholderFile: File = File(imagesDir, "avatar_placeholder.png")

    init {
        if (!placeholderFile.exists()) {
            copyPlaceholderToFilesDir()
        }
    }

    private fun copyPlaceholderToFilesDir() {
        val drawable = ContextCompat.getDrawable(context, R.drawable.avatar_placeholder)
            ?: throw IllegalArgumentException("Drawable not found: R.drawable.avatar_placeholder")

        val bitmap = drawable.toBitmap()
        placeholderFile.outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        }
    }

    suspend fun downloadAndSaveImage(imageUrl: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(imageUrl)
                    .build()

                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.w("ImageDownloader", "HTTP error: ${response.code}")
                        return@withContext placeholderFile.name
                    } else {
                        val byteStream = response.body.byteStream()

                        val fileName = "IMG_${UUID.randomUUID()}.jpg"
                        val file = File(imagesDir, fileName)

                        file.outputStream().use { output ->
                            byteStream.copyTo(output)
                        }

                        return@withContext file.name
                    }
                }


            } catch (e: Exception) {
                Log.e("ImageDownloader", "Failed to download image: $e")
            }
            return@withContext if (placeholderFile.exists()) placeholderFile.name else ""
        }
    }

    fun deleteImage(fileName: String) {
        val file = File(imagesDir, fileName)
        if (file.exists() && file.name != "avatar_placeholder.png") {
            file.delete()
        }
    }
}