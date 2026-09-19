package com.barathiraja.dinam.ui.screens.today

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.data.model.TodoItem
import com.barathiraja.dinam.ui.components.common.ComposerChip
import com.barathiraja.dinam.ui.components.common.DateItem
import com.barathiraja.dinam.ui.components.home.TodoRow
import com.barathiraja.dinam.ui.screens.past.PastOccurrenceScreen
import com.barathiraja.dinam.ui.screens.time.TimePickerScreen
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions
import java.time.LocalDate
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(
    todayViewModel: TodayViewModel,
    onBack: () -> Unit,
    onItemClick: (OccurrenceItemEntity) -> Unit
) {

    val uiState by todayViewModel.uiState.collectAsState()

    val today = remember {
        LocalDate.now()
    }

    val dates = remember(today) {
        (-30..30).map {
            today.plusDays(it.toLong())
        }
    }

    var selectedDate by remember {
        mutableStateOf(today)
    }

    val dateListState = rememberLazyListState(
        initialFirstVisibleItemIndex = 30
    )

    var itemText by remember {
        mutableStateOf("")
    }

    var selectedTime by remember {
        mutableStateOf("")
    }

    var repeatSelected by remember {
        mutableStateOf(false)
    }

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    fun addCurrentItem() {

        val text = itemText.trim()

        if (text.isEmpty()) {
            return
        }

        todayViewModel.addItem(
            text = text,
            remindAt = selectedTime.ifEmpty {
                null
            }
        )

        itemText = ""
        selectedTime = ""
        repeatSelected = false
    }

    LaunchedEffect(selectedDate) {

        todayViewModel.selectDate(
            periodDate = selectedDate.toString()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                DinamColors.Surface
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal,
                    top = DinamDimensions.screenTop
                )
        ) {

            Text(
                text = "< Home",
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextSecondary,
                modifier = Modifier.clickable {
                    onBack()
                }
            )

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.smallSpacing
                )
            )

            Text(
                text = if (selectedDate == today) {
                    "Today"
                } else {
                    formatFullDate(
                        selectedDate
                    )
                },
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

            LazyRow(
                state = dateListState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    4.dp
                )
            ) {

                items(
                    items = dates,
                    key = {
                        it.toString()
                    }
                ) { date ->

                    DateItem(
                        day = date.dayOfWeek.getDisplayName(
                            JavaTextStyle.SHORT,
                            Locale.US
                        ),
                        date = date.dayOfMonth.toString(),
                        selected = date == selectedDate,
                        modifier = Modifier.clickable {

                            selectedDate = date
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.sectionSpacing
                )
            )
        }

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

        AnimatedContent(
            targetState = selectedDate,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 220
                    )
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        durationMillis = 160
                    )
                )
            },
            label = "selected-date-content"
        ) { date ->

            if (date.isBefore(today)) {

                PastOccurrenceScreen(
                    selectedDate = date,
                    items = uiState.items,
                    onBack = onBack,
                    onItemCheckedChange = { item, checked ->

                        todayViewModel.setItemChecked(
                            item = item,
                            checked = checked
                        )
                    }
                )

            } else {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "${uiState.items.count { it.checked }} of ${uiState.items.size}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DinamColors.TextPrimary
                        )

                        Spacer(
                            modifier = Modifier.width(
                                8.dp
                            )
                        )

                        Text(
                            text = if (date == today) {
                                "done today"
                            } else {
                                "done"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = DinamColors.TextSecondary
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(
                            DinamDimensions.sectionSpacing
                        )
                    )

                    uiState.items.forEach { occurrenceItem ->

                        TodoRow(
                            item = TodoItem(
                                title = occurrenceItem.text,
                                time = occurrenceItem.remindAt,
                                checked = occurrenceItem.checked
                            ),
                            onCheckedChange = {

                                todayViewModel.setItemChecked(
                                    item = occurrenceItem,
                                    checked = !occurrenceItem.checked
                                )
                            },
                            onItemClick = {
                                onItemClick(occurrenceItem)
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(
                            4.dp
                        )
                    )

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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleLarge,
                            color = DinamColors.TextPrimary,
                            modifier = Modifier.clickable {
                                addCurrentItem()
                            }
                        )

                        Spacer(
                            modifier = Modifier.width(
                                14.dp
                            )
                        )

                        TextField(
                            value = itemText,
                            onValueChange = {
                                itemText = it
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(
                                    DinamDimensions.itemRowHeight
                                ),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = DinamColors.TextPrimary
                            ),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    addCurrentItem()
                                }
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor =
                                    DinamColors.Surface,
                                unfocusedContainerColor =
                                    DinamColors.Surface,
                                disabledContainerColor =
                                    DinamColors.Surface,
                                focusedIndicatorColor =
                                    androidx.compose.ui.graphics.Color.Transparent,
                                unfocusedIndicatorColor =
                                    androidx.compose.ui.graphics.Color.Transparent,
                                cursorColor =
                                    DinamColors.Primary
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 38.dp,
                                end = 0.dp
                            ),
                        horizontalArrangement = Arrangement.spacedBy(
                            DinamDimensions.chipSpacing
                        )
                    ) {

                        ComposerChip(
                            text = selectedTime.ifEmpty {
                                "Time"
                            },
                            selected = selectedTime.isNotEmpty(),
                            onClick = {
                                showTimePicker = true
                            }
                        )

                        ComposerChip(
                            text = "Every day",
                            selected = repeatSelected,
                            onClick = {
                                repeatSelected = !repeatSelected
                            }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(
                            14.dp
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                DinamColors.SurfaceMuted
                            )
                            .padding(
                                start = DinamDimensions.screenHorizontal,
                                end = DinamDimensions.screenHorizontal,
                                top = 12.dp,
                                bottom = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Both chips clear after each item, so the next one starts",
                            style = MaterialTheme.typography.labelMedium,
                            color = DinamColors.TextSecondary
                        )

                        Text(
                            text = "fresh.",
                            style = MaterialTheme.typography.labelMedium,
                            color = DinamColors.TextSecondary
                        )
                    }
                }
            }
        }
    }

    if (showTimePicker) {

        ModalBottomSheet(
            onDismissRequest = {
                showTimePicker = false
            },
            containerColor = DinamColors.Surface
        ) {

            TimePickerScreen(
                itemTitle = itemText.trim().ifEmpty {
                    "item"
                },
                initialTime = selectedTime.ifEmpty {
                    null
                },
                onDone = { time ->
                    selectedTime = time
                    showTimePicker = false
                },
                onClear = {
                    selectedTime = ""
                    showTimePicker = false
                },
                onDismiss = {
                    showTimePicker = false
                }
            )
        }
    }
}

private fun formatFullDate(
    date: LocalDate
): String {

    return date.format(
        java.time.format.DateTimeFormatter.ofPattern(
            "EEEE, d MMMM",
            Locale.US
        )
    )
}