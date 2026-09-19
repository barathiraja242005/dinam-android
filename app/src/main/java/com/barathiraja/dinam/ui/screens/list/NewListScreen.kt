package com.barathiraja.dinam.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import com.barathiraja.dinam.ui.components.common.swipeToBack
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamDimensions

private val listCategories = listOf(
    "grocery",
    "packing",
    "moving",
    "wedding",
    "home",
    "work",
    "errands",
    "health",
    "other"
)

@Composable
fun NewListScreen(
    onBack: () -> Unit,
    onCreateList: (
        title: String,
        category: String
    ) -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf("other")
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
            .padding(
                start = DinamDimensions.screenHorizontal,
                end = DinamDimensions.screenHorizontal,
                top = DinamDimensions.screenTop
            )
    ) {

        /*
         * -----------------------------------------------------
         * HEADER
         * -----------------------------------------------------
         */

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
            text = "New list",
            style = MaterialTheme.typography.headlineMedium,
            color = DinamColors.TextTitle
        )

        Spacer(
            modifier = Modifier.height(
                DinamDimensions.titleToContent
            )
        )

        /*
         * -----------------------------------------------------
         * TITLE
         * -----------------------------------------------------
         */

        Text(
            text = "List name",
            style = MaterialTheme.typography.bodyLarge,
            color = DinamColors.TextSecondary
        )

        Spacer(
            modifier = Modifier.height(
                8.dp
            )
        )

        TextField(
            value = title,
            onValueChange = {
                title = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text(
                    text = "e.g. Grocery — Trader Joe's",
                    color = DinamColors.TextMuted
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                color = DinamColors.TextPrimary
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor =
                    DinamColors.SurfaceMuted,
                unfocusedContainerColor =
                    DinamColors.SurfaceMuted,
                disabledContainerColor =
                    DinamColors.SurfaceMuted,
                focusedIndicatorColor =
                    DinamColors.Primary,
                unfocusedIndicatorColor =
                    DinamColors.Border
            ),
            shape = RoundedCornerShape(
                10.dp
            )
        )

        Spacer(
            modifier = Modifier.height(
                DinamDimensions.sectionSpacing
            )
        )

        /*
         * -----------------------------------------------------
         * CATEGORY
         * -----------------------------------------------------
         */

        Text(
            text = "Category",
            style = MaterialTheme.typography.bodyLarge,
            color = DinamColors.TextSecondary
        )

        Spacer(
            modifier = Modifier.height(
                10.dp
            )
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(
                8.dp
            )
        ) {

            listCategories
                .chunked(3)
                .forEach { rowCategories ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp
                        )
                    ) {

                        rowCategories.forEach { category ->

                            CategoryChip(
                                text = category,
                                selected =
                                    category ==
                                            selectedCategory,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    selectedCategory =
                                        category
                                }
                            )
                        }

                        repeat(
                            3 - rowCategories.size
                        ) {
                            Spacer(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
        }

        Spacer(
            modifier = Modifier.height(
                DinamDimensions.sectionSpacing
            )
        )

        /*
         * -----------------------------------------------------
         * CREATE
         * -----------------------------------------------------
         */

        val canCreate =
            title.trim().isNotEmpty()

        Text(
            text = "Create list",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp
            ),
            color = if (canCreate) {
                DinamColors.Surface
            } else {
                DinamColors.TextMuted
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (canCreate) {
                        DinamColors.Primary
                    } else {
                        DinamColors.SurfaceMuted
                    },
                    shape = RoundedCornerShape(
                        10.dp
                    )
                )
                .clickable(
                    enabled = canCreate
                ) {
                    onCreateList(
                        title.trim(),
                        selectedCategory
                    )
                }
                .padding(
                    vertical = 14.dp
                ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Text(
        text = text.replaceFirstChar {
            it.uppercase()
        },
        modifier = modifier
            .height(42.dp)
            .background(
                color = if (selected) {
                    DinamColors.PrimaryLight
                } else {
                    DinamColors.SurfaceMuted
                },
                shape = RoundedCornerShape(
                    20.dp
                )
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 8.dp,
                vertical = 10.dp
            ),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = if (selected) {
            DinamColors.Primary
        } else {
            DinamColors.TextSecondary
        }
    )
}