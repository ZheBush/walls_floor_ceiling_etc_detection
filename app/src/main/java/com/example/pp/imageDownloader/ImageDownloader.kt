package com.example.pp.imageDownloader

import android.app.DownloadManager
import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import androidx.core.net.toUri

class ImageDownloader(private val context: Context) {

    suspend fun downloadImageWithDownloadManager(
        imageUrl: String,
        fileName: String = "image_${System.currentTimeMillis()}.jpg"
    ): Long? = withContext(Dispatchers.IO) {
        try {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            val uri = imageUrl.toUri()
            val request = DownloadManager.Request(uri)
                .setTitle("Downloading image")
                .setDescription("Downloading $fileName")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "YourAppName/${fileName}"
                )
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            downloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun downloadImageManually(
        imageUrl: String,
        fileName: String = "image_${System.currentTimeMillis()}.jpg"
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection()
            connection.connect()

            val inputStream = connection.getInputStream()
            val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val appDir = File(picturesDir, "YourAppName")

            if (!appDir.exists()) {
                appDir.mkdirs()
            }

            val outputFile = File(appDir, fileName)
            val outputStream = FileOutputStream(outputFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(outputFile.absolutePath),
                arrayOf("image/jpeg"),
                null
            )

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}