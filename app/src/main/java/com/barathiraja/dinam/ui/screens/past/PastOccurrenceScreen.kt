package com.barathiraja.dinam.ui.screens.past

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.ui.components.common.swipeToBack
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PastOccurrenceScreen(
    selectedDate: LocalDate,
    items: List<OccurrenceItem>,
    onBack: () -> Unit,
    onItemCheckedChange: (
        item: OccurrenceItem,
        checked: Boolean
    ) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .swipeToBack(
                onBack = onBack
            )
            .padding(
                start = DinamDimensions.screenHorizontal,
                end = DinamDimensions.screenHorizontal
            )
    ) {

        Spacer(
            modifier = Modifier.height(
                DinamDimensions.sectionSpacing
            )
        )

        /*
         * Past occurrence content.
         *
         * The date itself is already displayed in the
         * shared date strip owned by TodayScreen.
         */
        Text(
            text = formatFullDate(
                selectedDate
            ),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = DinamColors.TextSecondary
        )

        Spacer(
            modifier = Modifier.height(
                DinamDimensions.sectionSpacing
            )
        )

        /*
         * Frozen past occurrence items.
         *
         * Only the checkbox remains interactive.
         */
        items.forEach { item ->

            PastOccurrenceRow(
                item = item,
                onCheckedChange = { checked ->

                    onItemCheckedChange(
                        item,
                        checked
                    )
                }
            )
        }
    }
}

@Composable
private fun PastOccurrenceRow(
    item: OccurrenceItem,
    onCheckedChange: (Boolean) -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DinamDimensions.itemRowHeight
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            PastCheckbox(
                checked = item.checked,
                onClick = {
                    onCheckedChange(
                        !item.checked
                    )
                }
            )

            Spacer(
                modifier = Modifier.width(
                    DinamDimensions.checkboxTextSpacing
                )
            )

            Text(
                text = item.text,
                modifier = Modifier
                    .weight(1f)
                    .alpha(
                        0.55f
                    ),
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

            if (item.remindAt != null) {

                Text(
                    text = item.remindAt,
                    modifier = Modifier.alpha(
                        0.55f
                    ),
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

@Composable
private fun PastCheckbox(
    checked: Boolean,
    onClick: () -> Unit
) {

    val size = DinamDimensions.checkboxSize

    Row(
        modifier = Modifier
            .width(size)
            .height(size)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (checked) {

            Text(
                text = "✓",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = DinamColors.Surface,
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .background(
                        color = DinamColors.Primary.copy(
                            alpha = 0.65f
                        ),
                        shape = RoundedCornerShape(
                            5.dp
                        )
                    )
                    .padding(
                        top = 1.dp
                    )
                    .wrapContentAlignment()
            )

        } else {

            Spacer(
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(
                            5.dp
                        )
                    )
            )
        }
    }
}

private fun formatFullDate(
    date: LocalDate
): String {

    return date.format(
        DateTimeFormatter.ofPattern(
            "EEEE, d MMMM",
            Locale.US
        )
    )
}

private fun Modifier.wrapContentAlignment(): Modifier {
    return this
}