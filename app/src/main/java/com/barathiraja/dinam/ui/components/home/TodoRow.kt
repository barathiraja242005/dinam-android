package com.barathiraja.dinam.ui.components.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.barathiraja.dinam.data.model.TodoItem
import com.barathiraja.dinam.ui.components.common.SwipeableTaskRow
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

@Composable
fun TodoRow(
    item: TodoItem,
    onCheckedChange: () -> Unit,
    onItemClick: () -> Unit = {},
    checkboxEnabled: Boolean = true,
    onSwipeRight: (() -> Unit)? = null,
    onSaveInlineEdit: ((newText: String) -> Unit)? = null
) {
    var isEditing by remember(item.id) {
        mutableStateOf(false)
    }

    var editText by remember(item.id, item.title) {
        mutableStateOf(item.title)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    SwipeableTaskRow(
        enabled = !isEditing,

        onSwipeLeft = {
            if (onSaveInlineEdit != null) {
                editText = item.title
                isEditing = true
            }
        },

        onSwipeRight = {
            Log.d(
                "DELETE_DEBUG",
                "TodoRow callback exists = ${onSwipeRight != null}, item=${item.title}"
            )

            if (onSwipeRight != null) {
                Log.d(
                    "DELETE_DEBUG",
                    "TodoRow INVOKING parent onSwipeRight"
                )

                onSwipeRight()
            } else {
                Log.d(
                    "DELETE_DEBUG",
                    "TodoRow onSwipeRight is NULL"
                )
            }
        }
    ) {
        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = DinamDimensions.itemRowHeight
                    )
                    .clickable(
                        enabled = !isEditing
                    ) {
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
                    onClick = onCheckedChange,
                    enabled = checkboxEnabled && !isEditing
                )

                Spacer(
                    modifier = Modifier.width(
                        DinamDimensions.checkboxTextSpacing
                    )
                )

                if (isEditing) {

                    BasicTextField(
                        value = editText,
                        onValueChange = {
                            editText = it
                        },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = DinamColors.TextPrimary
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()

                                val trimmed = editText.trim()

                                if (
                                    trimmed.isNotEmpty() &&
                                    onSaveInlineEdit != null
                                ) {
                                    onSaveInlineEdit(trimmed)
                                }

                                isEditing = false
                            }
                        )
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "✓",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DinamColors.Primary,
                        modifier = Modifier
                            .clickable {

                                keyboardController?.hide()

                                val trimmed = editText.trim()

                                if (
                                    trimmed.isNotEmpty() &&
                                    onSaveInlineEdit != null
                                ) {
                                    onSaveInlineEdit(trimmed)
                                }

                                isEditing = false
                            }
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                    )

                } else {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = item.title,
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

                        if (!item.listName.isNullOrEmpty()) {

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = item.listName,
                                style = MaterialTheme.typography.bodySmall,
                                color = DinamColors.TextSecondary
                            )
                        }
                    }

                    if (item.time != null) {

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = item.time,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = DinamColors.TextMuted
                            )
                        )
                    }
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
}