package com.barathiraja.dinam.ui.screens.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun ScopeSheet(
    itemTitle: String,
    onJustToday: () -> Unit,
    onTodayAndFuture: () -> Unit,
    onCancel: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                DinamColors.Surface
            )
            .padding(
                start = DinamDimensions.screenHorizontal,
                end = DinamDimensions.screenHorizontal,
                top = 10.dp,
                bottom = 18.dp
            )
    ) {

        /*
         * -----------------------------------------------------
         * SHEET HANDLE
         * -----------------------------------------------------
         */

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 150.dp
                )
                .height(4.dp)
                .background(
                    DinamColors.Border
                )
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        /*
         * -----------------------------------------------------
         * TITLE
         * -----------------------------------------------------
         */

        Text(
            text = "Remove $itemTitle",
            style = MaterialTheme.typography.titleLarge,
            color = DinamColors.TextTitle
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = "Earlier days keep their record either way.",
            style = MaterialTheme.typography.bodyMedium,
            color = DinamColors.TextSecondary
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        /*
         * -----------------------------------------------------
         * JUST TODAY
         * -----------------------------------------------------
         */

        ScopeOption(
            title = "Just today",
            description = "Stays on your list from tomorrow. Use this when you're skipping a day.",
            onClick = onJustToday
        )

        Divider()

        /*
         * -----------------------------------------------------
         * TODAY AND FUTURE
         * -----------------------------------------------------
         */

        ScopeOption(
            title = "Today and future days",
            description = "Removes it from your routine for good.",
            onClick = onTodayAndFuture
        )

        Divider()

        /*
         * -----------------------------------------------------
         * CANCEL
         * -----------------------------------------------------
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCancel()
                }
                .padding(
                    vertical = 18.dp
                )
        ) {

            Text(
                text = "Cancel",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextSecondary
            )
        }
    }
}

@Composable
private fun ScopeOption(
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                vertical = 12.dp
            )
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = DinamColors.TextPrimary
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = DinamColors.TextSecondary
        )
    }
}

@Composable
private fun Divider() {

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                DinamDimensions.dividerHeight
            )
            .background(
                DinamColors.BorderLight
            )
    )
}