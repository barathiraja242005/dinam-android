@file:Suppress("NewApi")

package com.barathiraja.dinam.ui.components.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.OverdueItem
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun OverdueSection(
    overdueItems: List<OverdueItem>,
    onRescheduleClick: (OccurrenceItem) -> Unit
) {
    if (overdueItems.isEmpty()) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Overdue",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFD93025)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.dividerHeight)
                .background(DinamColors.Border)
        )

        overdueItems.forEach { overdueItem ->
            val item = overdueItem.item
            val originalDateFormatted = formatOverdueDueDate(overdueItem.originalDate)

            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DinamDimensions.itemRowHeight),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyLarge,
                            color = DinamColors.TextPrimary
                        )
                        Text(
                            text = "Due $originalDateFormatted",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFD93025)
                        )
                    }

                    Text(
                        text = "Reschedule",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = DinamColors.Primary,
                        modifier = Modifier
                            .clickable { onRescheduleClick(item) }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DinamDimensions.dividerHeight)
                        .background(DinamColors.Border)
                )
            }
        }
    }
}

private fun formatOverdueDueDate(dateStr: String): String {
    return try {
        val date = LocalDate.parse(dateStr)
        date.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US))
    } catch (_: Exception) {
        dateStr
    }
}