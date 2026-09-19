package com.barathiraja.dinam.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun DateItem(
    day: String,
    date: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(DinamDimensions.dateItemWidth)
            .height(DinamDimensions.dateItemHeight)
            .then(
                if (selected) {
                    Modifier.background(
                        color = DinamColors.SelectedDate,
                        shape = RoundedCornerShape(
                            DinamDimensions.dateItemRadius
                        )
                    )
                } else {
                    Modifier
                }
            )
            .padding(
                top = 8.dp,
                bottom = 7.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = day,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) {
                DinamColors.TextLight
            } else {
                DinamColors.TextMuted
            }
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = date,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                DinamColors.Surface
            } else {
                DinamColors.TextPrimary
            }
        )
    }
}