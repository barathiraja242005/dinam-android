package com.barathiraja.dinam.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.domain.model.DinamList
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.util.DateProvider
import com.barathiraja.dinam.ui.components.home.OverdueSection
import com.barathiraja.dinam.ui.components.home.TodoRow
import com.barathiraja.dinam.ui.screens.today.TodayViewModel
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    todayViewModel: TodayViewModel,
    onOpenToday: () -> Unit,
    onOpenList: (DinamList) -> Unit,
    onCreateList: () -> Unit,
    onItemClick: (OccurrenceItem) -> Unit = {},
    onRescheduleItem: (OccurrenceItem) -> Unit = {},
    onDeleteItem: (OccurrenceItem) -> Unit = {},
    onSaveInlineEdit: (item: OccurrenceItem, newText: String) -> Unit = { _, _ -> }
) {

    LaunchedEffect(Unit) {
        todayViewModel.selectDate(
            DateProvider.todayString()
        )
    }

    val uiState by homeViewModel.uiState.collectAsState()

    val todayState by todayViewModel.uiState.collectAsState()

    val todayItems =
        todayState.items

    val completedCount =
        todayItems.count {
            it.checked
        }

    val todayDateText =
        formatHomeDate(
            DateProvider.todayString()
        )

    var itemText by remember {
        mutableStateOf("")
    }

    var selectedTime by remember {
        mutableStateOf("")
    }

    fun addCurrentItem() {

        val text =
            itemText.trim()

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
    }

    Scaffold(
        containerColor = DinamColors.Surface,
        floatingActionButton = {

            FloatingActionButton(
                onClick = onCreateList,
                modifier = Modifier
                    .navigationBarsPadding()
                    .size(64.dp),
                shape = RoundedCornerShape(50),
                containerColor = DinamColors.Primary,
                contentColor = DinamColors.Surface
            ) {

                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    DinamColors.Surface
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    start = 32.dp,
                    end = 26.dp,
                    top = DinamDimensions.screenTop,
                    bottom = 32.dp
                )
                .padding(innerPadding)
        ) {

            /*
             * -------------------------------------------------
             * TODAY HEADER
             * -------------------------------------------------
             */

            Text(
                text = "Today",
                style = MaterialTheme.typography.headlineMedium,
                color = DinamColors.TextTitle
            )

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.smallSpacing / 2
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        DinamDimensions.navigationTouchHeight
                    )
                    .clickable {
                        onOpenToday()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = todayDateText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "·",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "$completedCount of ${todayItems.size} done",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "›",
                    style = MaterialTheme.typography.titleLarge,
                    color = DinamColors.TextSecondary
                )
            }

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.titleToContent
                )
            )

            /*
             * -------------------------------------------------
             * TODAY ITEMS
             * -------------------------------------------------
             */

            todayItems.forEach { item ->

                TodoRow(
                    item = item.toTodoItem(),
                    onCheckedChange = {

                        todayViewModel.setItemChecked(
                            item = item,
                            checked = !item.checked
                        )
                    },
                    onItemClick = {
                        onItemClick(item)
                    },
                    onSwipeRight = {
                        onDeleteItem(item)
                    },
                    onSaveInlineEdit = { newText ->
                        onSaveInlineEdit(item, newText)
                    }
                )
            }

            /*
             * -------------------------------------------------
             * OVERDUE ITEMS
             * -------------------------------------------------
             */

            if (todayState.overdueItems.isNotEmpty()) {
                OverdueSection(
                    overdueItems = todayState.overdueItems,
                    onRescheduleClick = { item ->
                        onRescheduleItem(item)
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            /*
             * -------------------------------------------------
             * HOME ADD ITEM COMPOSER
             * -------------------------------------------------
             */

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
                    modifier = Modifier.width(14.dp)
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
                    placeholder = {
                        Text(
                            text = "Add item",
                            color = DinamColors.TextMuted
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 18.dp.value.sp,
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
                            Color.Transparent,
                        unfocusedIndicatorColor =
                            Color.Transparent,
                        cursorColor =
                            DinamColors.Primary
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(42.dp)
            )

            /*
             * -------------------------------------------------
             * LISTS
             * -------------------------------------------------
             */

            Text(
                text = "Lists",
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextSecondary
            )

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.sectionSpacing
                )
            )

            if (
                uiState.lists.isEmpty() &&
                !uiState.isLoading
            ) {

                Text(
                    text = "No lists yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextMuted
                )

            } else {

                uiState.lists.forEach { list ->

                    ChecklistRow(
                        list = list,
                        onClick = {
                            onOpenList(list)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChecklistRow(
    list: DinamList,
    onClick: () -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DinamDimensions.itemRowHeight
                )
                .clickable {
                    onClick()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = list.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextPrimary
            )
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

private fun OccurrenceItem.toTodoItem():
        com.barathiraja.dinam.data.model.TodoItem {

    return com.barathiraja.dinam.data.model.TodoItem(
        title = text,
        time = remindAt,
        checked = checked,
        listName = listName
    )
}

private fun formatHomeDate(
    date: String
): String {

    return try {

        val parsedDate =
            LocalDate.parse(date)

        val formatter =
            DateTimeFormatter.ofPattern(
                "EEEE d MMMM",
                Locale.US
            )

        parsedDate.format(
            formatter
        )

    } catch (_: Exception) {

        date
    }
}