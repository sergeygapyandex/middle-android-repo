package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private object CustomContainerConstants {
    const val SIZE_CHECK_DELAY_MS = 50L
}

@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)? = null,
    secondChild: @Composable (() -> Unit)? = null,
    alphaAnimationDurationMs: Int = 2000,
    translationAnimationDurationMs: Int = 5000,
) {
    val parentHeightPx = remember { mutableFloatStateOf(0f) }
    val firstHeightPx = remember { mutableFloatStateOf(0f) }
    val secondHeightPx = remember { mutableFloatStateOf(0f) }
    val gapPx = with(LocalDensity.current) { 10.dp.toPx() }

    val firstAlpha = remember { Animatable(0f) }
    val secondAlpha = remember { Animatable(0f) }
    val firstTranslateYPx = remember { Animatable(0f) }
    val secondTranslateYPx = remember { Animatable(0f) }

    val firstStarted = remember { mutableStateOf(false) }
    val secondStarted = remember { mutableStateOf(false) }

    val bothChildrenPresent = firstChild != null && secondChild != null
    val halfGap = if (bothChildrenPresent) gapPx / 2f else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                parentHeightPx.floatValue = coords.size.height.toFloat()
            },
        contentAlignment = Alignment.Center
    ) {
        firstChild?.let { child ->
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = firstAlpha.value
                        translationY = firstTranslateYPx.value
                    }
                    .onGloballyPositioned { firstHeightPx.floatValue = it.size.height.toFloat() },
                contentAlignment = Alignment.Center
            ) { child() }

            LaunchedEffect(Unit) {
                while (parentHeightPx.floatValue <= 0f || firstHeightPx.floatValue <= 0f) {
                    delay(CustomContainerConstants.SIZE_CHECK_DELAY_MS)
                }

                if (!firstStarted.value) {
                    firstStarted.value = true

                    val parentH = parentHeightPx.floatValue
                    val h = firstHeightPx.floatValue
                    val targetY = -(parentH / 2f) + (h / 2f)
                    val startY = -(halfGap + h / 2f)

                    firstTranslateYPx.snapTo(startY)

                    launch {
                        firstAlpha.animateTo(1f, animationSpec = tween(alphaAnimationDurationMs))
                    }
                    launch {
                        firstTranslateYPx.animateTo(targetY, animationSpec = tween(translationAnimationDurationMs))
                    }
                }
            }
        }

        secondChild?.let { child ->
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = secondAlpha.value
                        translationY = secondTranslateYPx.value
                    }
                    .onGloballyPositioned { secondHeightPx.floatValue = it.size.height.toFloat() },
                contentAlignment = Alignment.Center
            ) { child() }

            LaunchedEffect(Unit) {
                while (parentHeightPx.floatValue <= 0f || secondHeightPx.floatValue <= 0f) {
                    delay(CustomContainerConstants.SIZE_CHECK_DELAY_MS)
                }

                if (!secondStarted.value) {
                    secondStarted.value = true

                    val parentH = parentHeightPx.floatValue
                    val h = secondHeightPx.floatValue
                    val targetY = +(parentH / 2f) - (h / 2f)
                    val startY = +(halfGap + h / 2f)

                    secondTranslateYPx.snapTo(startY)

                    launch {
                        secondAlpha.animateTo(1f, animationSpec = tween(alphaAnimationDurationMs))
                    }
                    launch {
                        secondTranslateYPx.animateTo(targetY, animationSpec = tween(translationAnimationDurationMs))
                    }
                }
            }
        }
    }
}
