package com.barathiraja.dinam.ui.screens.today

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.data.model.TodoItem
import com.barathiraja.dinam.ui.components.common.ComposerChip
import com.barathiraja.dinam.ui.components.common.DateItem
import com.barathiraja.dinam.ui.components.home.TodoRow
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun TodayScreen(
    onBack: () -> Unit
) {

    var itemText by remember {
        mutableStateOf("Evening tablet")
    }

    var selectedTime by remember {
        mutableStateOf("21:30")
    }

    var repeatSelected by remember {
        mutableStateOf(true)
    }

    val todos = remember {
        mutableStateOf(
            listOf(
                TodoItem("Run", "6:30", true),
                TodoItem("Morning tablet", "8:00", true),
                TodoItem("Vitamin D", "8:00", true),
                TodoItem("Evening tablet", "21:30", false),
                TodoItem("Ten minutes of reading", "21:30", false),
                TodoItem("Make the bed", null, false)
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DinamColors.Surface)
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
                text = "Today",
                style = MaterialTheme.typography.headlineMedium,
                color = DinamColors.TextTitle
            )

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.titleToContent
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                DateItem(
                    day = "Mon",
                    date = "8",
                    selected = false
                )

                DateItem(
                    day = "Tue",
                    date = "9",
                    selected = false
                )

                DateItem(
                    day = "Wed",
                    date = "10",
                    selected = false
                )

                DateItem(
                    day = "Thu",
                    date = "11",
                    selected = false
                )

                DateItem(
                    day = "Fri",
                    date = "12",
                    selected = true
                )
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
                .background(DinamColors.BorderLight)
        )

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
                    text = "3 of 6",
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = DinamColors.TextPrimary
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = "done today",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )
            }

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.sectionSpacing
                )
            )

            todos.value.forEachIndexed { index, item ->

                TodoRow(
                    item = item,
                    onCheckedChange = {

                        val updatedTodos =
                            todos.value.toMutableList()

                        updatedTodos[index] = item.copy(
                            checked = !item.checked
                        )

                        todos.value = updatedTodos
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DinamDimensions.dividerHeight
                )
                .background(DinamColors.BorderLight)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = DinamDimensions.screenHorizontal,
                    end = DinamDimensions.screenHorizontal
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "+",
                style = MaterialTheme.typography.titleLarge,
                color = DinamColors.TextPrimary
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
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = DinamColors.TextPrimary
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = DinamColors.Surface,
                    unfocusedContainerColor = DinamColors.Surface,
                    disabledContainerColor = DinamColors.Surface,
                    focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    cursorColor = DinamColors.Primary
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 64.dp,
                    end = DinamDimensions.screenHorizontal
                ),
            horizontalArrangement = Arrangement.spacedBy(
                DinamDimensions.chipSpacing
            )
        ) {

            ComposerChip(
                text = selectedTime,
                selected = selectedTime.isNotEmpty(),
                onClick = { }
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
            modifier = Modifier.height(14.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DinamColors.SurfaceMuted)
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