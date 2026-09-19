package com.barathiraja.dinam.ui.components.common

import android.util.Log
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableTaskRow(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minSwipeDistance: Dp = 40.dp,
    onSwipeLeft: (() -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (!enabled) {
        Box(modifier = modifier) {
            content()
        }
        return
    }

    var offsetX by remember { mutableStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()

    val density = LocalDensity.current

    val minDistancePx = remember(density, minSwipeDistance) {
        with(density) {
            minSwipeDistance.toPx()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(enabled, onSwipeLeft, onSwipeRight) {

                if (!enabled) {
                    return@pointerInput
                }

                detectHorizontalDragGestures(

                    onDragStart = {
                        Log.d(
                            "SWIPE_DEBUG",
                            "DRAG START"
                        )

                        offsetX = 0f
                    },

                    onDragEnd = {

                        val finalOffset = offsetX

                        Log.d(
                            "SWIPE_DEBUG",
                            "DRAG END | offset=$finalOffset | threshold=$minDistancePx | right=${finalOffset >= minDistancePx} | left=${finalOffset <= -minDistancePx}"
                        )

                        if (
                            finalOffset >= minDistancePx &&
                            onSwipeRight != null
                        ) {

                            Log.d(
                                "SWIPE_DEBUG",
                                "CALLING onSwipeRight"
                            )

                            onSwipeRight()

                        } else if (
                            finalOffset <= -minDistancePx &&
                            onSwipeLeft != null
                        ) {

                            Log.d(
                                "SWIPE_DEBUG",
                                "CALLING onSwipeLeft"
                            )

                            onSwipeLeft()

                        } else {

                            Log.d(
                                "SWIPE_DEBUG",
                                "NO SWIPE CALLBACK"
                            )
                        }

                        coroutineScope.launch {

                            val anim = Animatable(offsetX)

                            anim.animateTo(
                                targetValue = 0f,
                                animationSpec = spring()
                            ) {
                                offsetX = value
                            }
                        }
                    },

                    onDragCancel = {

                        Log.d(
                            "SWIPE_DEBUG",
                            "DRAG CANCEL | offset=$offsetX"
                        )

                        coroutineScope.launch {

                            val anim = Animatable(offsetX)

                            anim.animateTo(
                                targetValue = 0f,
                                animationSpec = spring()
                            ) {
                                offsetX = value
                            }
                        }
                    },

                    onHorizontalDrag = { change, dragAmount ->

                        change.consume()

                        val newOffset =
                            (offsetX + dragAmount)
                                .coerceIn(
                                    -180f,
                                    180f
                                )

                        offsetX = newOffset

                        Log.d(
                            "SWIPE_DEBUG",
                            "DRAGGING | offset=$offsetX | dragAmount=$dragAmount"
                        )
                    }
                )
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        offsetX.roundToInt(),
                        0
                    )
                }
        ) {
            content()
        }
    }
}