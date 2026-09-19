package com.barathiraja.dinam.ui.components.common

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.swipeToBack(
    enabled: Boolean = true,
    minSwipeDistance: Dp = 70.dp,
    onBack: () -> Unit
): Modifier = if (!enabled) this else Modifier.pointerInput(onBack) {
    var totalDrag = 0f
    val minDistancePx = minSwipeDistance.toPx()

    detectHorizontalDragGestures(
        onDragStart = {
            totalDrag = 0f
        },
        onDragEnd = {
            if (totalDrag >= minDistancePx) {
                onBack()
            }
            totalDrag = 0f
        },
        onDragCancel = {
            totalDrag = 0f
        },
        onHorizontalDrag = { _, dragAmount ->
            if (dragAmount > 0 || totalDrag > 0) {
                totalDrag += dragAmount
                if (totalDrag < 0f) {
                    totalDrag = 0f
                }
            }
        }
    )
}