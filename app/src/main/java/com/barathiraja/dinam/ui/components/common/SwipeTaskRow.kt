package com.barathiraja.dinam.ui.components.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableTaskRow(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val thresholdPx = remember(density) { with(density) { 60.dp.toPx() } }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(enabled, onSwipeLeft, onSwipeRight) {
                if (!enabled) return@pointerInput
                detectHorizontalDragGestures(
                    onDragStart = { offsetX = 0f },
                    onDragEnd = {
                        val finalOffset = offsetX
                        if (finalOffset <= -thresholdPx && onSwipeLeft != null) {
                            onSwipeLeft()
                        } else if (finalOffset >= thresholdPx && onSwipeRight != null) {
                            onSwipeRight()
                        }
                        coroutineScope.launch {
                            val anim = Animatable(offsetX)
                            anim.animateTo(0f, spring()) {
                                offsetX = value
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            val anim = Animatable(offsetX)
                            anim.animateTo(0f, spring()) {
                                offsetX = value
                            }
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        val newOffset = (offsetX + dragAmount).coerceIn(-150f, 150f)
                        offsetX = newOffset
                    }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offsetX.roundToInt(), 0) }
        ) {
            content()
        }
    }
}