@file:Suppress("NewApi")

package com.barathiraja.dinam.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import com.barathiraja.dinam.domain.util.DateProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.ui.components.common.swipeToBack
import com.barathiraja.dinam.ui.screens.time.TimePickerScreen
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemDetailScreen(
    item: ListItem,
    onBack: () -> Unit,
    onSaveItem: (updatedItem: ListItem) -> Unit,
    onDeleteItem: (item: ListItem) -> Unit
) {
    var selectedDate by remember(item.id) { mutableStateOf(item.dueDate) }
    var selectedTime by remember(item.id) { mutableStateOf(item.remindAt) }
    var remindMe by remember(item.id) { mutableStateOf(item.remindMe) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val isReminderEnabled = !selectedDate.isNullOrEmpty() && !selectedTime.isNullOrEmpty()

    fun updateItem(
        date: String? = selectedDate,
        time: String? = selectedTime,
        reminder: Boolean = remindMe
    ) {
        val validReminder = if (!date.isNullOrEmpty() && !time.isNullOrEmpty()) reminder else false
        val updated = item.copy(
            dueDate = date,
            remindAt = time,
            remindMe = validReminder
        )
        onSaveItem(updated)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DinamColors.Surface)
            .swipeToBack(onBack = onBack)
    ) {
        /*
         * HEADER
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
                    .height(DinamDimensions.navigationTouchHeight)
                    .clickable { onBack() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "‹ Back",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(DinamDimensions.smallSpacing))

            Text(
                text = item.text,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = DinamColors.TextTitle
            )

            Spacer(modifier = Modifier.height(DinamDimensions.titleToContent))
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.dividerHeight)
                .background(DinamColors.BorderLight)
        )

        /*
         * DATE ROW
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.itemRowHeight)
                .clickable { showDatePicker = true }
                .padding(horizontal = DinamDimensions.screenHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Date",
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = selectedDate?.let { formatDisplayDate(it) } ?: "Set date",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedDate != null) DinamColors.TextPrimary else DinamColors.TextMuted
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.dividerHeight)
                .background(DinamColors.BorderLight)
        )

        /*
         * TIME ROW
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.itemRowHeight)
                .clickable { showTimePicker = true }
                .padding(horizontal = DinamDimensions.screenHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time",
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextPrimary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = selectedTime ?: "Set time",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedTime != null) DinamColors.TextPrimary else DinamColors.TextMuted
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.dividerHeight)
                .background(DinamColors.BorderLight)
        )

        /*
         * REMIND ME TOGGLE ROW
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = DinamDimensions.screenHorizontal,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Remind me",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isReminderEnabled) DinamColors.TextPrimary else DinamColors.TextMuted
                )
                Text(
                    text = if (isReminderEnabled) "Notification scheduled for date & time" else "Set both date and time to enable reminder",
                    style = MaterialTheme.typography.bodySmall,
                    color = DinamColors.TextSecondary
                )
            }

            Switch(
                checked = isReminderEnabled && remindMe,
                enabled = isReminderEnabled,
                onCheckedChange = { checked ->
                    remindMe = checked
                    updateItem(reminder = checked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DinamColors.Surface,
                    checkedTrackColor = DinamColors.Primary
                )
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.dividerHeight)
                .background(DinamColors.BorderLight)
        )

        /*
         * REMOVE ITEM BUTTON
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.itemRowHeight)
                .clickable { onDeleteItem(item) }
                .padding(horizontal = DinamDimensions.screenHorizontal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Remove item",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    /*
     * DATE PICKER DIALOG
     */
    if (showDatePicker) {
        val todayStartUtcMillis = remember {
            DateProvider.today()
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli()
        }

        val initialMillis = try {
            selectedDate?.let {
                LocalDate.parse(it)
                    .atStartOfDay(ZoneId.of("UTC"))
                    .toInstant()
                    .toEpochMilli()
            }
        } catch (_: Exception) {
            null
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialMillis ?: todayStartUtcMillis,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis >= todayStartUtcMillis
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year >= DateProvider.today().year
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val newDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .toString()
                            selectedDate = newDate
                            val newReminder = isReminderEnabled && remindMe
                            updateItem(date = newDate, reminder = newReminder)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = DinamColors.Primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = DinamColors.TextSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    /*
     * TIME PICKER MODAL SHEET
     */
    if (showTimePicker) {
        ModalBottomSheet(
            onDismissRequest = { showTimePicker = false },
            containerColor = DinamColors.Surface
        ) {
            TimePickerScreen(
                itemTitle = item.text,
                initialTime = selectedTime,
                onDone = { time ->
                    selectedTime = time
                    val newReminder = isReminderEnabled && remindMe
                    updateItem(time = time, reminder = newReminder)
                    showTimePicker = false
                },
                onClear = {
                    selectedTime = null
                    remindMe = false
                    updateItem(time = null, reminder = false)
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

private fun formatDisplayDate(dateStr: String): String {
    return try {
        val date = LocalDate.parse(dateStr)
        date.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.US))
    } catch (_: Exception) {
        dateStr
    }
}