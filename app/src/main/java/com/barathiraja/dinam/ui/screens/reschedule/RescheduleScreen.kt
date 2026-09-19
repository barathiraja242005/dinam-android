@file:Suppress("NewApi")

package com.barathiraja.dinam.ui.screens.reschedule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.util.DateProvider
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
fun RescheduleScreen(
    item: OccurrenceItem,
    onBack: () -> Unit,
    onRescheduleChanged: (
        item: OccurrenceItem,
        newDate: String,
        newTime: String?,
        newRemindMe: Boolean
    ) -> Unit
) {
    var selectedDate by remember(item.id) {
        mutableStateOf(DateProvider.todayString())
    }
    var selectedTime by remember(item.id) {
        mutableStateOf<String?>(item.remindAt ?: "5:00 PM")
    }
    var remindMe by remember(item.id) {
        mutableStateOf(false)
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val isReminderEnabled = selectedDate.isNotEmpty() && !selectedTime.isNullOrEmpty()

    fun updateReschedule(
        date: String = selectedDate,
        time: String? = selectedTime,
        reminder: Boolean = remindMe
    ) {
        val validReminder = if (date.isNotEmpty() && !time.isNullOrEmpty()) reminder else false
        onRescheduleChanged(item, date, time, validReminder)
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
                    text = "‹ List",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.text,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = DinamColors.TextTitle
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

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

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(DinamColors.SurfaceMuted)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = formatPillDate(selectedDate),
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextPrimary
                )
            }
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
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = DinamColors.Primary
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
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "A one-time notification at this time.",
                    style = MaterialTheme.typography.bodySmall,
                    color = DinamColors.TextSecondary
                )
            }

            Switch(
                checked = isReminderEnabled && remindMe,
                enabled = isReminderEnabled,
                onCheckedChange = { checked ->
                    remindMe = checked
                    updateReschedule(reminder = checked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DinamColors.Surface,
                    checkedTrackColor = DinamColors.Primary,
                    uncheckedThumbColor = DinamColors.Surface,
                    uncheckedTrackColor = DinamColors.BorderLight
                )
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(DinamDimensions.dividerHeight)
                .background(DinamColors.BorderLight)
        )
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
            LocalDate.parse(selectedDate)
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant()
                .toEpochMilli()
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
                            updateReschedule(date = newDate)
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
                    updateReschedule(time = time)
                    showTimePicker = false
                },
                onClear = {
                    selectedTime = null
                    remindMe = false
                    updateReschedule(time = null, reminder = false)
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

private fun formatPillDate(dateStr: String): String {
    return try {
        val date = LocalDate.parse(dateStr)
        date.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US))
    } catch (_: Exception) {
        dateStr
    }
}