@file:Suppress("NewApi")

package com.barathiraja.dinam.ui.screens.list

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.barathiraja.dinam.R
import com.barathiraja.dinam.ui.components.common.SwipeableTaskRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.domain.model.DinamList
import com.barathiraja.dinam.domain.model.ListItem
import androidx.compose.foundation.layout.fillMaxSize
import com.barathiraja.dinam.domain.util.Canonicalizer
import com.barathiraja.dinam.ui.components.common.swipeToBack
import com.barathiraja.dinam.ui.components.home.CheckmarkBox
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun ListDetailScreen(
    list: DinamList,
    items: List<ListItem>,
    onBack: () -> Unit,
    onItemCheckedChange: (
        item: ListItem,
        checked: Boolean
    ) -> Unit,
    onAddFromYourLists: () -> Unit,
    onAddItem: () -> Unit,
    onItemClick: (ListItem) -> Unit = {},
    onDeleteItem: (ListItem) -> Unit = {},
    onSaveInlineEdit: (item: ListItem, newText: String) -> Unit = { _, _ -> },
    viewModel: ListDetailViewModel? = null
) {

    val viewModelState =
        viewModel?.uiState?.collectAsState()

    val currentItems =
        viewModelState?.value?.items ?: items

    var itemText by remember {
        mutableStateOf("")
    }

    fun addCurrentItem() {

        val text =
            itemText.trim()

        if (text.isEmpty()) {
            return
        }

        if (viewModel != null) {

            viewModel.addItem(
                listId = list.id,
                text = text
            )

            itemText = ""

        } else {

            onAddItem()
        }
    }

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
                    text = "‹ Home",
                    style = MaterialTheme.typography.bodyLarge,
                    color = DinamColors.TextSecondary
                )
            }

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = list.title,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = DinamColors.TextTitle
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = list.category.replaceFirstChar {
                    it.uppercase()
                },
                style = MaterialTheme.typography.bodyLarge,
                color = DinamColors.TextSecondary
            )

            Spacer(
                modifier = Modifier.height(
                    DinamDimensions.titleToContent
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

        /*
         * -----------------------------------------------------
         * LIST ITEMS + ADD ITEM
         * -----------------------------------------------------
         */

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(
                items = currentItems,
                key = {
                    it.id
                }
            ) { item ->

                ListDetailItemRow(
                    item = item,
                    onCheckedChange = { checked ->

                        if (viewModel != null) {

                            viewModel.setItemChecked(
                                item = item,
                                checked = checked
                            )

                        } else {

                            onItemCheckedChange(
                                item,
                                checked
                            )
                        }
                    },
                    onItemClick = {
                        onItemClick(item)
                    },
                    onSaveInlineEdit = { newText ->
                        if (viewModel != null) {
                            viewModel.updateItem(
                                item.copy(
                                    text = newText,
                                    canonicalId = Canonicalizer.canonicalId(newText)
                                )
                            )
                        } else {
                            onSaveInlineEdit(item, newText)
                        }
                    },
                    onSwipeRight = {
                        if (viewModel != null) {
                            viewModel.deleteItem(item)
                        } else {
                            onDeleteItem(item)
                        }
                    }
                )
            }

            /*
             * -------------------------------------------------
             * ADD ITEM
             * -------------------------------------------------
             */

            item {

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
                        .fillMaxWidth()
                        .height(
                            DinamDimensions.itemRowHeight
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "+",
                        fontSize = 24.sp,
                        color = DinamColors.TextPrimary,
                        modifier = Modifier
                            .clickable {
                                addCurrentItem()
                            }
                            .padding(
                                start =
                                    DinamDimensions.screenHorizontal
                            )
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
                                Color.Transparent,
                            unfocusedIndicatorColor =
                                Color.Transparent,
                            cursorColor =
                                DinamColors.Primary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ListDetailItemRow(
    item: ListItem,
    onCheckedChange: (Boolean) -> Unit,
    onItemClick: () -> Unit = {},
    onSaveInlineEdit: ((newText: String) -> Unit)? = null,
    onSwipeRight: (() -> Unit)? = null
) {
    var isEditing by remember(item.id) { mutableStateOf(false) }
    var editText by remember(item.id, item.text) { mutableStateOf(item.text) }

    val keyboardController = LocalSoftwareKeyboardController.current

    SwipeableTaskRow(
        enabled = !isEditing,
        onSwipeLeft = {
            if (onSaveInlineEdit != null) {
                editText = item.text
                isEditing = true
            }
        },
        onSwipeRight = onSwipeRight
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = DinamDimensions.itemRowHeight)
                    .clickable(enabled = !isEditing) {
                        onItemClick()
                    }
                    .padding(
                        start = 10.dp,
                        end = DinamDimensions.screenHorizontal,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckmarkBox(
                    checked = item.checked,
                    onClick = {
                        onCheckedChange(!item.checked)
                    },
                    enabled = !isEditing
                )

                Spacer(modifier = Modifier.width(DinamDimensions.checkboxTextSpacing))

                if (isEditing) {
                    BasicTextField(
                        value = editText,
                        onValueChange = { editText = it },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = DinamColors.TextPrimary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                val trimmed = editText.trim()
                                if (trimmed.isNotEmpty() && onSaveInlineEdit != null) {
                                    onSaveInlineEdit(trimmed)
                                }
                                isEditing = false
                            }
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "Edit",
                        tint = DinamColors.Primary,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                keyboardController?.hide()
                                val trimmed = editText.trim()
                                if (trimmed.isNotEmpty() && onSaveInlineEdit != null) {
                                    onSaveInlineEdit(trimmed)
                                }
                                isEditing = false
                            }
                    )
                } else {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.text,
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

                        val detailsText = listOfNotNull(
                            item.dueDate?.let { formatDisplayShortDate(it) },
                            item.remindAt
                        ).joinToString(" · ")

                        if (detailsText.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = detailsText,
                                style = MaterialTheme.typography.bodySmall,
                                color = DinamColors.TextSecondary
                            )
                        }
                    }

                    if (item.remindMe && !item.dueDate.isNullOrEmpty() && !item.remindAt.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "🔔",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DinamDimensions.dividerHeight)
                    .background(DinamColors.BorderLight)
            )
        }
    }
}

private fun formatDisplayShortDate(dateStr: String): String {
    return try {
        val date = LocalDate.parse(dateStr)
        date.format(DateTimeFormatter.ofPattern("MMM d", Locale.US))
    } catch (_: Exception) {
        dateStr
    }
}