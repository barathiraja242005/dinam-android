package com.barathiraja.dinam.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.barathiraja.dinam.data.model.TodoItem
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun TodoRow(
    item: TodoItem,
    onCheckedChange: () -> Unit,
    onItemClick: () -> Unit = {},
    checkboxEnabled: Boolean = true
) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DinamDimensions.itemRowHeight
                )
                .clickable {
                    onItemClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            CheckmarkBox(
                checked = item.checked,
                onClick = onCheckedChange,
                enabled = checkboxEnabled
            )

            Spacer(
                modifier = Modifier.width(
                    DinamDimensions.checkboxTextSpacing
                )
            )

            Text(
                text = item.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = if (item.checked) {
                        DinamColors.TextMuted
                    } else {
                        DinamColors.TextPrimary
                    }
                ),
                textDecoration = if (item.checked) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )

            if (item.time != null) {

                Text(
                    text = item.time,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = DinamColors.TextMuted
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DinamDimensions.dividerHeight
                )
                .background(
                    DinamColors.Border
                )
        )
    }
}