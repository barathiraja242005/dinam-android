package com.barathiraja.dinam.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun CheckmarkBox(
    checked: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(DinamDimensions.checkboxSize)
            .clickable(
                onClick = onClick
            )
            .then(
                if (!checked) {
                    Modifier.border(
                        width = 1.5.dp,
                        color = DinamColors.CheckboxBorder,
                        shape = RoundedCornerShape(6.dp)
                    )
                } else {
                    Modifier
                }
            )
            .background(
                color = if (checked) {
                    DinamColors.Primary
                } else {
                    DinamColors.Surface
                },
                shape = RoundedCornerShape(6.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Text(
                text = "✓",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}