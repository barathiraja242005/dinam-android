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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import com.barathiraja.dinam.ui.components.common.swipeToBack
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun ItemDetailScreen(
    itemTitle: String,
    itemTime: String?,
    everyDayEnabled: Boolean,
    onBack: () -> Unit,
    onTimeClick: () -> Unit,
    onEveryDayChanged: (Boolean) -> Unit,
    onRemoveItem: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                DinamColors.Surface
            )
            .swipeToBack(
                onBack = onBack
            )
    ) {

        /*
         * -----------------------------------------------------
         * HEADER
         * -----------------------------------------------------
         */

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal,
                    top = DinamDimensions.screenTop
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        DinamDimensions.navigationTouchHeight
                    )
                    .clickable {
                        onBack()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "‹ Today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = itemTitle,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = DinamColors.TextTitle
            )

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.titleToContent
                )
            )
        }

        Divider()

        /*
         * -----------------------------------------------------
         * TIME
         * -----------------------------------------------------
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onTimeClick()
                }
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal,
                    top = 18.dp,
                    bottom = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Time",
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = itemTime ?: "Set time",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp
                ),
                color = DinamColors.TextPrimary
            )
        }

        Divider()

        /*
         * -----------------------------------------------------
         * EVERY DAY
         * -----------------------------------------------------
         *
         * The switch is now controlled by the persisted state
         * supplied by the caller.
         *
         * It is intentionally NOT stored in local remember state.
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal,
                    top = 18.dp,
                    bottom = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Every day",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextPrimary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Keep this item on your daily list.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DinamColors.TextSecondary
                )
            }

            Switch(
                checked = everyDayEnabled,
                onCheckedChange = onEveryDayChanged
            )
        }

        Divider()

        /*
         * -----------------------------------------------------
         * REMINDER — PLUS FEATURE
         * -----------------------------------------------------
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal,
                    top = 18.dp,
                    bottom = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Remind me at",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextMuted
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Coming later",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DinamColors.TextLight
                )
            }

            Text(
                text = "Plus",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DinamColors.Primary
            )
        }

        Divider()

        /*
         * -----------------------------------------------------
         * REMOVE ITEM
         * -----------------------------------------------------
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onRemoveItem()
                }
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal,
                    top = 20.dp,
                    bottom = 20.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Remove item",
                style = MaterialTheme.typography.bodyLarge,
                color = androidx.compose.ui.graphics.Color(
                    0xFFD64545
                )
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        /*
         * -----------------------------------------------------
         * FOOTNOTE
         * -----------------------------------------------------
         */

        Text(
            text = "Changes to this item affect today and future days. Past days stay unchanged.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal
                ),
            style = MaterialTheme.typography.bodyMedium,
            color = DinamColors.TextMuted
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