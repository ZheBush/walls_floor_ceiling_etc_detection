package com.example.pp.imageDownloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImageViewModel(private val imageDownloader: ImageDownloader): ViewModel() {
    fun downloadImage(imageUrl: String) {
        viewModelScope.launch {
            imageDownloader.downloadImageWithDownloadManager(imageUrl)
        }
    }
}