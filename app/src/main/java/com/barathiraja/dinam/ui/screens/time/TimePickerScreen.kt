package com.barathiraja.dinam.ui.screens.time

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.ui.theme.DinamColors
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun TimePickerScreen(
    itemTitle: String,
    initialTime: String?,
    onDone: (String) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {

    val initialLocalTime = remember(initialTime) {
        parseInitialTime(initialTime)
    }

    var selectedHour by remember(initialTime) {
        mutableStateOf(initialLocalTime.hour)
    }

    var selectedMinute by remember(initialTime) {
        mutableStateOf(
            (initialLocalTime.minute / 5) * 5
        )
    }

    val hours = remember {
        (0..23).toList()
    }

    val minutes = remember {
        (0..55 step 5).toList()
    }

    val hourListState = rememberLazyListState()
    val minuteListState = rememberLazyListState()

    LaunchedEffect(initialTime) {

        val hourIndex = hours.indexOf(selectedHour)

        if (hourIndex >= 0) {
            hourListState.scrollToItem(
                index = hourIndex
            )
        }

        val minuteIndex = minutes.indexOf(selectedMinute)

        if (minuteIndex >= 0) {
            minuteListState.scrollToItem(
                index = minuteIndex
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DinamColors.Surface,
                shape = RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                )
            )
    ) {

        /*
         * Title
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 26.dp,
                    end = 26.dp,
                    top = 16.dp
                )
        ) {

            Text(
                text = "Time for “$itemTitle”",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                color = DinamColors.TextPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Times order your list. They don't send anything yet.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp
                ),
                color = DinamColors.TextSecondary
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        /*
         * 24-hour time wheel
         */
        TimeSelectionArea(
            hours = hours,
            minutes = minutes,
            selectedHour = selectedHour,
            selectedMinute = selectedMinute,
            hourListState = hourListState,
            minuteListState = minuteListState,
            onHourSelected = {
                selectedHour = it
            },
            onMinuteSelected = {
                selectedMinute = it
            }
        )

        /*
         * Bottom actions
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(
                    start = 14.dp,
                    end = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextButton(
                onClick = onClear
            ) {

                Text(
                    text = "Clear",
                    color = DinamColors.TextSecondary,
                    fontSize = 17.sp
                )
            }

            TextButton(
                onClick = {

                    val formattedTime =
                        formatSelectedTime(
                            hour = selectedHour,
                            minute = selectedMinute
                        )

                    onDone(formattedTime)
                }
            ) {

                Text(
                    text = "Done",
                    color = DinamColors.Primary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TimeSelectionArea(
    hours: List<Int>,
    minutes: List<Int>,
    selectedHour: Int,
    selectedMinute: Int,
    hourListState: LazyListState,
    minuteListState: LazyListState,
    onHourSelected: (Int) -> Unit,
    onMinuteSelected: (Int) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(
                start = 26.dp,
                end = 26.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        SelectionColumn(
            values = hours,
            selectedValue = selectedHour,
            state = hourListState,
            formatter = { value ->
                value.toString()
            },
            onSelected = onHourSelected,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = ":",
            fontSize = 22.sp,
            color = DinamColors.TextPrimary,
            modifier = Modifier.padding(
                horizontal = 4.dp
            )
        )

        SelectionColumn(
            values = minutes,
            selectedValue = selectedMinute,
            state = minuteListState,
            formatter = { value ->
                value.toString().padStart(2, '0')
            },
            onSelected = onMinuteSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun <T> SelectionColumn(
    values: List<T>,
    selectedValue: T,
    state: LazyListState,
    formatter: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .height(210.dp),
        contentAlignment = Alignment.Center
    ) {

        /*
         * Selected row
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = DinamColors.SurfaceMuted,
                    shape = RoundedCornerShape(6.dp)
                )
        )

        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Spacer(
                    modifier = Modifier.height(77.dp)
                )
            }

            items(values) { value ->

                val selected = value == selectedValue

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clickable {
                            onSelected(value)
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = formatter(value),
                        fontSize = if (selected) {
                            22.sp
                        } else {
                            21.sp
                        },
                        fontWeight = FontWeight.Normal,
                        color = if (selected) {
                            DinamColors.TextPrimary
                        } else {
                            DinamColors.TextLight
                        }
                    )
                }
            }

            item {
                Spacer(
                    modifier = Modifier.height(77.dp)
                )
            }
        }
    }
}

private fun parseInitialTime(
    initialTime: String?
): LocalTime {

    /*
     * No existing time:
     * open at the nearest 5-minute value.
     */
    if (initialTime.isNullOrBlank()) {

        val now = LocalTime.now()

        val roundedMinute =
            (
                    (now.minute / 5f).roundToInt() * 5
                    ) % 60

        val roundedHour = if (
            roundedMinute == 0 &&
            now.minute >= 58
        ) {
            (now.hour + 1) % 24
        } else {
            now.hour
        }

        return LocalTime.of(
            roundedHour,
            roundedMinute
        )
    }

    /*
     * Existing time.
     */
    return try {

        LocalTime.parse(
            initialTime,
            DateTimeFormatter.ofPattern(
                "HH:mm",
                Locale.US
            )
        )

    } catch (_: Exception) {

        LocalTime.now()
    }
}

private fun formatSelectedTime(
    hour: Int,
    minute: Int
): String {

    return String.format(
        Locale.US,
        "%02d:%02d",
        hour,
        minute
    )
}