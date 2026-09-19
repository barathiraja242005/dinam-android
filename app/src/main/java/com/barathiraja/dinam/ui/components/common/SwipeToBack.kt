package com.barathiraja.dinam.ui.components.common

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

fun Modifier.swipeToBack(
    enabled: Boolean = true,
    minSwipeDistance: Dp = 60.dp,
    onBack: () -> Unit
): Modifier = if (!enabled) this else Modifier.pointerInput(onBack) {
    val minDistancePx = minSwipeDistance.toPx()

    awaitEachGesture {
        val down = awaitFirstDown(pass = PointerEventPass.Initial, requireUnconsumed = false)
        var totalX = 0f
        var totalY = 0f
        var isBackGesture = false

        while (true) {
            val event = awaitPointerEvent(pass = PointerEventPass.Initial)
            val change = event.changes.firstOrNull { it.id == down.id } ?: break

            if (!change.pressed) {
                if (isBackGesture && totalX >= minDistancePx) {
                    onBack()
                }
                break
            }

            val drag = change.positionChange()
            totalX += drag.x
            totalY += drag.y

            if (!isBackGesture) {
                if (totalX > 20f && totalX > abs(totalY) * 1.2f) {
                    isBackGesture = true
                }
            }

            if (isBackGesture) {
                change.consume()
            }
        }
    }
}