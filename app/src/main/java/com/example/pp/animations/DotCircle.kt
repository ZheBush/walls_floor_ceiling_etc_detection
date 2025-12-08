package com.example.pp.animations

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pp.ui.theme.Blue64
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DotCircle(
    dotCount: Int = 8,
    circleRadius: Int = 4,
    dotRadius: Float = 6f,
    dotColor: Color = Blue64
) {

    val dotCount = 8
    val infiniteTransition = rememberInfiniteTransition()
    val rotations = List(dotCount) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1900,
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
            val radius = size.width / circleRadius
            repeat(dotCount) { index ->
                val angle = (index * 360f / dotCount) + rotations[index].value
                val x = center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
                val y = center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

                drawCircle(
                    color = dotColor.copy(alpha = 0.5f),
                    center = Offset(x, y),
                    radius = dotRadius
                )
            }
        }
    }
}