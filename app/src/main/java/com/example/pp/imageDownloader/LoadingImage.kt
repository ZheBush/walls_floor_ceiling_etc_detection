package com.example.pp.imageDownloader

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.pp.ui.theme.Blue64
import kotlin.math.cos
import kotlin.math.sin


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
                DotCircle()
            }
        }
    )
}

@Composable
fun DotCircle() {

    val dotCount = 8
    val infiniteTransition = rememberInfiniteTransition()
    val rotations = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200,
                    easing = LinearEasing
                )
            )
        )
    }

    Box(
        modifier = Modifier.size(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 3
            repeat(dotCount) { index ->
                val angle = (index * 360f / dotCount) + rotations[index].value
                val x = center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

                drawCircle(
                    color = Blue64.copy(alpha = 0.7f),
                    center = Offset(x, y),
                    radius = 4f
                )
            }
        }
    }

}