package com.barathiraja.dinam.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.barathiraja.dinam.data.model.Checklist
import com.barathiraja.dinam.data.model.TodoItem
import com.barathiraja.dinam.ui.components.home.TodoRow
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun HomeScreen(
    onOpenToday: () -> Unit
) {

    val todos = remember {
        mutableStateListOf(
            TodoItem("Run", "6:30", true),
            TodoItem("Morning tablet", "8:00", true),
            TodoItem("Vitamin D", "8:00", true),
            TodoItem("Evening tablet", "21:30", false),
            TodoItem("Ten minutes of reading", "21:30", false),
            TodoItem("Make the bed", null, false)
        )
    }

    val lists = remember {
        listOf(
            Checklist("Grocery — Trader Joe's", "4/12"),
            Checklist("Move to Oakland", "9/31"),
            Checklist("Packing — Tahoe", ""),
            Checklist("Bike service", "2/3")
        )
    }

    Scaffold(
        containerColor = DinamColors.Surface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
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
                .background(DinamColors.Surface)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 32.dp,
                    end = 26.dp,
                    top = 50.dp,
                    bottom = 32.dp
                )
                .padding(innerPadding)
        ) {

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
                modifier = Modifier.clickable {
                    onOpenToday()
                },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Friday 12 September",
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
                    text = "3 of 6 done",
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

            todos.forEachIndexed { index, item ->

                TodoRow(
                    item = item,
                    onCheckedChange = {
                        todos[index] = item.copy(
                            checked = !item.checked
                        )
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            AddItemRow()

            Spacer(
                modifier = Modifier.height(42.dp)
            )

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

            lists.forEach { list ->

                ChecklistRow(
                    checklist = list
                )
            }
        }
    }
}

@Composable
private fun AddItemRow() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "+",
            style = MaterialTheme.typography.titleLarge,
            color = DinamColors.TextLight
        )

        Spacer(
            modifier = Modifier.width(
                DinamDimensions.checkboxTextSpacing
            )
        )

        Text(
            text = "Add item",
            style = MaterialTheme.typography.bodyLarge,
            color = DinamColors.TextMuted
        )
    }
}

@Composable
private fun ChecklistRow(
    checklist: Checklist
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

            Text(
                text = checklist.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextPrimary
            )

            if (checklist.progress.isNotEmpty()) {

                Text(
                    text = checklist.progress,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextMuted
                )
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    DinamDimensions.dividerHeight
                )
                .background(DinamColors.Border)
        )
    }
}