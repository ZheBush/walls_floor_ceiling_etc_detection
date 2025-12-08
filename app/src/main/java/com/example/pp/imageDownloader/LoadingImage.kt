package com.example.pp.imageDownloader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.SubcomposeAsyncImage
import com.example.pp.animations.DotCircle
import com.example.pp.ui.theme.Blue64


@Composable
fun LoadingImage(
    url: String,
    modifier: Modifier
) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = "loading image",
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
        loading = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DotCircle(
                    dotCount = 8,
                    circleRadius = 4,
                    dotRadius = 6f,
                    dotColor = Blue64
                )
            }
        }
    )
}

