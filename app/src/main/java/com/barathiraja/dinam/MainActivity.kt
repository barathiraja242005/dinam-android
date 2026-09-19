package com.barathiraja.dinam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.barathiraja.dinam.data.local.entity.ListEntity
import com.barathiraja.dinam.data.local.entity.ListItemEntity
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.data.repository.Repositories
import com.barathiraja.dinam.ui.screens.home.HomeScreen
import com.barathiraja.dinam.ui.screens.home.HomeViewModel
import com.barathiraja.dinam.ui.screens.home.HomeViewModelFactory
import com.barathiraja.dinam.ui.screens.item.ItemDetailScreen
import com.barathiraja.dinam.ui.screens.item.ScopeSheet
import com.barathiraja.dinam.ui.screens.list.ListDetailScreen
import com.barathiraja.dinam.ui.screens.list.ListDetailViewModel
import com.barathiraja.dinam.ui.screens.list.ListDetailViewModelFactory
import com.barathiraja.dinam.ui.screens.list.NewListScreen
import com.barathiraja.dinam.ui.screens.time.TimePickerScreen
import com.barathiraja.dinam.ui.screens.today.TodayScreen
import com.barathiraja.dinam.ui.screens.today.TodayViewModel
import com.barathiraja.dinam.ui.screens.today.TodayViewModelFactory
import com.barathiraja.dinam.ui.theme.DinamColors
import com.barathiraja.dinam.ui.theme.DinamTheme
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.LocalDate
import java.util.Locale
import java.util.UUID

class MainActivity : ComponentActivity() {

    private val repositories: Repositories
        get() = (application as DinamApplication).repositories

    private val todayViewModel: TodayViewModel by lazy {
        ViewModelProvider(
            this,
            TodayViewModelFactory(
                todayOccurrenceService =
                    repositories.todayOccurrenceService,
                userRepository =
                    repositories.userRepository
            )
        )[TodayViewModel::class.java]
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            HomeViewModelFactory(
                listRepository =
                    repositories.listRepository,
                userRepository =
                    repositories.userRepository
            )
        )[HomeViewModel::class.java]
    }

    private val listDetailViewModel: ListDetailViewModel by lazy {
        ViewModelProvider(
            this,
            ListDetailViewModelFactory(
                listItemRepository =
                    repositories.listItemRepository
            )
        )[ListDetailViewModel::class.java]
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        enableEdgeToEdge()

        setContent {

            DinamTheme {

                DinamApp(
                    todayViewModel = todayViewModel,
                    homeViewModel = homeViewModel,
                    listDetailViewModel =
                        listDetailViewModel,
                    repositories = repositories
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DinamApp(
    todayViewModel: TodayViewModel,
    homeViewModel: HomeViewModel,
    listDetailViewModel: ListDetailViewModel,
    repositories: Repositories
) {

    var showToday by remember {
        mutableStateOf(false)
    }

    var showNewList by remember {
        mutableStateOf(false)
    }

    var selectedItem by remember {
        mutableStateOf<OccurrenceItemEntity?>(null)
    }

    var selectedTodayItem by remember {
        mutableStateOf<TodayItemEntity?>(null)
    }

    var everyDayEnabled by remember {
        mutableStateOf(false)
    }

    var editedItemTime by remember {
        mutableStateOf<String?>(null)
    }

    var showItemTimePicker by remember {
        mutableStateOf(false)
    }

    var showScopeSheet by remember {
        mutableStateOf(false)
    }

    var selectedList by remember {
        mutableStateOf<ListEntity?>(null)
    }

    var selectedListItems by remember {
        mutableStateOf<List<ListItemEntity>>(
            emptyList()
        )
    }

    var showAddListItemDialog by remember {
        mutableStateOf(false)
    }

    var newListItemText by remember {
        mutableStateOf("")
    }

    val coroutineScope =
        rememberCoroutineScope()

    when {

        /*
         * -----------------------------------------------------
         * ITEM DETAIL
         * -----------------------------------------------------
         */

        selectedItem != null -> {

            ItemDetailScreen(
                itemTitle = selectedItem!!.text,
                itemTime = editedItemTime,
                everyDayEnabled = everyDayEnabled,

                onBack = {
                    selectedItem = null
                    selectedTodayItem = null
                    editedItemTime = null
                    everyDayEnabled = false
                    showScopeSheet = false
                },

                onTimeClick = {
                    showItemTimePicker = true
                },

                onEveryDayChanged = { enabled ->

                    val todayItem =
                        selectedTodayItem

                    if (todayItem != null) {

                        everyDayEnabled = enabled

                        coroutineScope.launch {

                            repositories
                                .todayOccurrenceService
                                .setEveryDay(
                                    userId =
                                        todayItem.userId,
                                    item = todayItem,
                                    enabled = enabled
                                )
                        }
                    }
                },

                onRemoveItem = {
                    showScopeSheet = true
                }
            )

            /*
             * -------------------------------------------------
             * ITEM TIME PICKER
             * -------------------------------------------------
             */

            if (showItemTimePicker) {

                ModalBottomSheet(
                    onDismissRequest = {
                        showItemTimePicker = false
                    },
                    containerColor = DinamColors.Surface
                ) {

                    TimePickerScreen(
                        itemTitle = selectedItem!!.text,
                        initialTime = editedItemTime,

                        onDone = { time ->

                            editedItemTime = time
                            showItemTimePicker = false
                        },

                        onClear = {

                            editedItemTime = null
                            showItemTimePicker = false
                        },

                        onDismiss = {
                            showItemTimePicker = false
                        }
                    )
                }
            }

            /*
             * -------------------------------------------------
             * REMOVE ITEM SCOPE SHEET
             * -------------------------------------------------
             */

            if (showScopeSheet) {

                ModalBottomSheet(
                    onDismissRequest = {
                        showScopeSheet = false
                    },
                    containerColor = DinamColors.Surface
                ) {

                    ScopeSheet(
                        itemTitle = selectedItem!!.text,

                        onJustToday = {

                            val todayItem =
                                selectedTodayItem

                            if (todayItem != null) {

                                coroutineScope.launch {

                                    repositories
                                        .todayOccurrenceService
                                        .removeJustToday(
                                            userId =
                                                todayItem.userId,
                                            todayItemId =
                                                todayItem.id,
                                            periodDate =
                                                LocalDate.now()
                                                    .toString()
                                        )

                                    showScopeSheet = false
                                    selectedItem = null
                                    selectedTodayItem = null
                                    editedItemTime = null
                                    everyDayEnabled = false

                                    todayViewModel.refreshSelectedDate()
                                }
                            } else {

                                showScopeSheet = false
                            }
                        },

                        onTodayAndFuture = {

                            val todayItem = selectedTodayItem

                            if (todayItem != null) {

                                coroutineScope.launch {

                                    repositories
                                        .todayOccurrenceService
                                        .removeTodayAndFuture(
                                            userId = todayItem.userId,
                                            todayItemId = todayItem.id,
                                            periodDate = LocalDate.now().toString()
                                        )

                                    showScopeSheet = false
                                    selectedItem = null
                                    selectedTodayItem = null
                                    editedItemTime = null
                                    everyDayEnabled = false

                                    todayViewModel.refreshSelectedDate()
                                }

                            } else {

                                showScopeSheet = false
                            }
                        },

                        onCancel = {
                            showScopeSheet = false
                        }
                    )
                }
            }
        }

        /*
         * -----------------------------------------------------
         * LIST DETAIL
         * -----------------------------------------------------
         */

        selectedList != null -> {

            ListDetailScreen(
                list = selectedList!!,
                items = selectedListItems,

                onBack = {

                    selectedList = null
                    selectedListItems = emptyList()
                    showAddListItemDialog = false
                    newListItemText = ""

                    homeViewModel.loadLists()
                },

                onItemCheckedChange = {
                        item,
                        checked ->

                    listDetailViewModel.setItemChecked(
                        item = item,
                        checked = checked
                    )
                },

                onAddFromYourLists = {
                    /*
                     * Suggestion sheet removed from V1 UI.
                     */
                },

                onAddItem = {

                    newListItemText = ""
                    showAddListItemDialog = true
                },

                viewModel =
                    listDetailViewModel
            )

            /*
             * -------------------------------------------------
             * ADD LIST ITEM DIALOG
             * -------------------------------------------------
             */

            if (showAddListItemDialog) {

                AlertDialog(
                    onDismissRequest = {

                        showAddListItemDialog = false
                        newListItemText = ""
                    },

                    title = {
                        Text(
                            text = "Add item"
                        )
                    },

                    text = {

                        TextField(
                            value = newListItemText,
                            onValueChange = {
                                newListItemText = it
                            },
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxWidth(),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Item name"
                                )
                            }
                        )
                    },

                    confirmButton = {

                        Button(
                            onClick = {

                                val text =
                                    newListItemText.trim()

                                if (text.isNotEmpty()) {

                                    val currentList =
                                        selectedList

                                    if (currentList != null) {

                                        listDetailViewModel.addItem(
                                            listId =
                                                currentList.id,
                                            text = text,
                                            canonicalId =
                                                canonicalId(text)
                                        )
                                    }

                                    showAddListItemDialog =
                                        false

                                    newListItemText = ""
                                }
                            }
                        ) {
                            Text(
                                text = "Add"
                            )
                        }
                    },

                    dismissButton = {

                        TextButton(
                            onClick = {

                                showAddListItemDialog = false
                                newListItemText = ""
                            }
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                    }
                )
            }
        }

        /*
         * -----------------------------------------------------
         * NEW LIST
         * -----------------------------------------------------
         */

        showNewList -> {

            NewListScreen(

                onBack = {
                    showNewList = false
                },

                onCreateList = {
                        title,
                        category ->

                    coroutineScope.launch {

                        val user =
                            repositories.userRepository
                                .getUser()

                        val userEntity =
                            if (user != null) {

                                user

                            } else {

                                val newUser =
                                    com.barathiraja.dinam
                                        .data.local.entity
                                        .UserEntity(
                                            id =
                                                UUID.randomUUID()
                                                    .toString()
                                        )

                                repositories.userRepository
                                    .createUser(
                                        newUser
                                    )

                                newUser
                            }

                        val newList =
                            ListEntity(
                                id =
                                    UUID.randomUUID()
                                        .toString(),
                                userId =
                                    userEntity.id,
                                title = title,
                                category = category,
                                archivedAt = null,
                                createdAt =
                                    System.currentTimeMillis()
                            )

                        repositories.listRepository
                            .insertList(
                                newList
                            )

                        selectedList = newList
                        selectedListItems = emptyList()

                        listDetailViewModel.loadItems(
                            listId = newList.id
                        )

                        showNewList = false

                        homeViewModel.loadLists()
                    }
                }
            )
        }

        /*
         * -----------------------------------------------------
         * TODAY
         * -----------------------------------------------------
         */

        showToday -> {

            TodayScreen(
                todayViewModel = todayViewModel,

                onBack = {
                    showToday = false
                },

                onItemClick = { item ->

                    selectedItem = item
                    editedItemTime = item.remindAt

                    coroutineScope.launch {

                        val todayItems =
                            repositories
                                .todayRepository
                                .getItemsForDate(
                                    userId =
                                        item.todayItemId
                                            ?.let {
                                                repositories
                                                    .userRepository
                                                    .getUser()
                                                    ?.id
                                            }
                                            ?: return@launch,
                                    periodDate =
                                        LocalDate.now()
                                            .toString()
                                )

                        val matchingTodayItem =
                            todayItems.firstOrNull {
                                it.id ==
                                        item.todayItemId
                            }

                        selectedTodayItem =
                            matchingTodayItem

                        everyDayEnabled =
                            matchingTodayItem != null &&
                                    matchingTodayItem.activeUntil == null
                    }
                }
            )
        }

        /*
         * -----------------------------------------------------
         * HOME
         * -----------------------------------------------------
         */

        else -> {

            HomeScreen(
                homeViewModel = homeViewModel,
                todayViewModel = todayViewModel,

                onOpenToday = {
                    showToday = true
                },

                onOpenList = { list ->

                    selectedList = list

                    coroutineScope.launch {

                        selectedListItems =
                            repositories
                                .listItemRepository
                                .getItemsForList(
                                    listId = list.id
                                )
                    }

                    listDetailViewModel.loadItems(
                        listId = list.id
                    )
                },

                onCreateList = {
                    showNewList = true
                }
            )
        }
    }
}

private fun canonicalId(
    text: String
): String {

    val normalized =
        text
            .lowercase(Locale.US)
            .replace(
                Regex("[^a-z0-9\\s]"),
                " "
            )
            .replace(
                Regex("\\s+"),
                " "
            )
            .trim()

    val digest =
        MessageDigest
            .getInstance("SHA-256")
            .digest(
                normalized.toByteArray()
            )

    return digest.joinToString("") {
        "%02x".format(it)
    }
}