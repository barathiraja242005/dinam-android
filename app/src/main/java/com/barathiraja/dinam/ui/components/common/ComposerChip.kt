package com.barathiraja.dinam.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun ComposerChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
            .background(
                color = if (selected) {
                    DinamColors.PrimaryLight
                } else {
                    DinamColors.Surface
                },
                shape = RoundedCornerShape(
                    DinamDimensions.chipRadius
                )
            )
            .border(
                width = 1.dp,
                color = if (selected) {
                    DinamColors.PrimaryLight
                } else {
                    DinamColors.Border
                },
                shape = RoundedCornerShape(
                    DinamDimensions.chipRadius
                )
            )
            .padding(
                horizontal = DinamDimensions.chipHorizontalPadding,
                vertical = DinamDimensions.chipVerticalPadding
            ),
        style = MaterialTheme.typography.bodyMedium,
        color = DinamColors.Primary
    )
}