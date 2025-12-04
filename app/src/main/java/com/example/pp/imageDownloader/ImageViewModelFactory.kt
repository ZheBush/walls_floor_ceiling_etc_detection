package com.example.pp.imageDownloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageViewModelFactory(
    private val imageDownloader: ImageDownloader
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            return ImageViewModel(imageDownloader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}