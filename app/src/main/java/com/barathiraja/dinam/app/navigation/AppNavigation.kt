@file:Suppress("NewApi")

package com.barathiraja.dinam.app.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.barathiraja.dinam.data.repository.Repositories
import com.barathiraja.dinam.domain.model.DinamList
import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.ui.screens.home.HomeScreen
import com.barathiraja.dinam.ui.screens.home.HomeViewModel
import com.barathiraja.dinam.ui.screens.item.ItemDetailScreen
import com.barathiraja.dinam.ui.screens.item.ScopeSheet
import com.barathiraja.dinam.ui.screens.list.ListDetailScreen
import com.barathiraja.dinam.ui.screens.list.ListDetailViewModel
import com.barathiraja.dinam.ui.screens.list.ListItemDetailScreen
import com.barathiraja.dinam.ui.screens.list.NewListScreen
import com.barathiraja.dinam.ui.screens.reschedule.RescheduleScreen
import com.barathiraja.dinam.ui.screens.time.TimePickerScreen
import com.barathiraja.dinam.ui.screens.today.TodayScreen
import com.barathiraja.dinam.ui.screens.today.TodayViewModel
import com.barathiraja.dinam.ui.theme.DinamColors
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    todayViewModel: TodayViewModel,
    homeViewModel: HomeViewModel,
    listDetailViewModel: ListDetailViewModel,
    repositories: Repositories,
    navController: NavHostController = rememberNavController()
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf<OccurrenceItem?>(null) }
    var selectedTodayItem by remember { mutableStateOf<TodayItem?>(null) }
    var everyDayEnabled by remember { mutableStateOf(false) }
    var editedItemTime by remember { mutableStateOf<String?>(null) }
    var showItemTimePicker by remember { mutableStateOf(false) }
    var showScopeSheet by remember { mutableStateOf(false) }
    var showDeleteItemConfirmDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<OccurrenceItem?>(null) }
    var rescheduleItem by remember { mutableStateOf<OccurrenceItem?>(null) }

    var selectedList by remember { mutableStateOf<DinamList?>(null) }
    var selectedListItem by remember { mutableStateOf<ListItem?>(null) }
    var selectedListItems by remember { mutableStateOf<List<ListItem>>(emptyList()) }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        /*
         * HOME
         */
        composable(NavRoutes.Home.route) {
            HomeScreen(
                homeViewModel = homeViewModel,
                todayViewModel = todayViewModel,
                onOpenToday = {
                    navController.navigate(NavRoutes.Today.route)
                },
                onOpenList = { list ->
                    selectedList = list
                    coroutineScope.launch {
                        selectedListItems = repositories.listItemRepository.getItemsForList(list.id)
                    }
                    listDetailViewModel.loadItems(list.id)
                    navController.navigate(NavRoutes.ListDetail.createRoute(list.id))
                },
                onCreateList = {
                    navController.navigate(NavRoutes.NewList.route)
                },
                onRescheduleItem = { item ->
                    rescheduleItem = item
                    navController.navigate(NavRoutes.Reschedule.createRoute(item.id))
                },
                onItemClick = { item ->
                    if (item.origin == "list_item") {
                        coroutineScope.launch {
                            val listItem = repositories.listItemRepository
                                .getItemsForList(item.occurrenceId)
                                .firstOrNull { it.id == item.id }
                            if (listItem != null) {
                                selectedListItem = listItem
                                navController.navigate(NavRoutes.ListItemDetail.createRoute(item.id))
                            }
                        }
                    } else {
                        selectedItem = item
                        editedItemTime = item.remindAt
                        coroutineScope.launch {
                            val selectedDate = todayViewModel.uiState.value.selectedDate
                            val user = repositories.userSession.getCurrentUser()
                            val todayItems = repositories.todayRepository.getItemsForDate(user.id, selectedDate)
                            val matchingTodayItem = todayItems.firstOrNull { it.id == item.todayItemId }
                                ?: item.todayItemId?.let { repositories.todayRepository.getItemById(it) }

                            selectedTodayItem = matchingTodayItem
                            everyDayEnabled = matchingTodayItem != null && matchingTodayItem.activeUntil == null
                            navController.navigate(NavRoutes.ItemDetail.createRoute(item.id))
                        }
                    }
                },
                onSaveInlineEdit = { item, newText ->
                    todayViewModel.renameItem(item = item, newText = newText)
                },
                onDeleteItem = { item ->
                    selectedItem = null
                    selectedListItem = null
                    itemToDelete = item

                    coroutineScope.launch {
                        val selectedDate = todayViewModel.uiState.value.selectedDate
                        val user = repositories.userSession.getCurrentUser()
                        val todayItems = repositories.todayRepository.getItemsForDate(user.id, selectedDate)
                        val matchingTodayItem = todayItems.firstOrNull { it.id == item.todayItemId }
                            ?: item.todayItemId?.let { repositories.todayRepository.getItemById(it) }

                        selectedTodayItem = matchingTodayItem
                        everyDayEnabled = matchingTodayItem != null && matchingTodayItem.activeUntil == null

                        if (everyDayEnabled) {
                            showScopeSheet = true
                        } else {
                            showDeleteItemConfirmDialog = true
                        }
                    }
                }
            )
        }

        /*
         * TODAY
         */
        composable(NavRoutes.Today.route) {
            TodayScreen(
                todayViewModel = todayViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onItemClick = { item ->
                    if (item.origin == "list_item") {
                        coroutineScope.launch {
                            val listItem = repositories.listItemRepository
                                .getItemsForList(item.occurrenceId)
                                .firstOrNull { it.id == item.id }
                            if (listItem != null) {
                                selectedListItem = listItem
                                navController.navigate(NavRoutes.ListItemDetail.createRoute(item.id))
                            }
                        }
                    } else {
                        selectedItem = item
                        editedItemTime = item.remindAt
                        coroutineScope.launch {
                            val selectedDate = todayViewModel.uiState.value.selectedDate
                            val user = repositories.userSession.getCurrentUser()
                            val todayItems = repositories.todayRepository.getItemsForDate(user.id, selectedDate)
                            val matchingTodayItem = todayItems.firstOrNull { it.id == item.todayItemId }
                                ?: item.todayItemId?.let { repositories.todayRepository.getItemById(it) }

                            selectedTodayItem = matchingTodayItem
                            everyDayEnabled = matchingTodayItem != null && matchingTodayItem.activeUntil == null
                            navController.navigate(NavRoutes.ItemDetail.createRoute(item.id))
                        }
                    }
                },
                onSaveInlineEdit = { item, newText ->
                    todayViewModel.renameItem(item = item, newText = newText)
                },
                onDeleteItem = { item ->
                    selectedItem = null
                    selectedListItem = null
                    itemToDelete = item

                    coroutineScope.launch {
                        val selectedDate = todayViewModel.uiState.value.selectedDate
                        val user = repositories.userSession.getCurrentUser()
                        val todayItems = repositories.todayRepository.getItemsForDate(user.id, selectedDate)
                        val matchingTodayItem = todayItems.firstOrNull { it.id == item.todayItemId }
                            ?: item.todayItemId?.let { repositories.todayRepository.getItemById(it) }

                        selectedTodayItem = matchingTodayItem
                        everyDayEnabled = matchingTodayItem != null && matchingTodayItem.activeUntil == null

                        if (everyDayEnabled) {
                            showScopeSheet = true
                        } else {
                            showDeleteItemConfirmDialog = true
                        }
                    }
                }
            )
        }

        /*
         * LIST DETAIL
         */
        composable(NavRoutes.ListDetail.route) {
            val list = selectedList
            if (list != null) {
                ListDetailScreen(
                    list = list,
                    items = selectedListItems,
                    onBack = {
                        navController.popBackStack()
                        homeViewModel.loadLists()
                        todayViewModel.refreshSelectedDate()
                    },
                    onItemCheckedChange = { item, checked ->
                        listDetailViewModel.setItemChecked(item = item, checked = checked)
                        todayViewModel.refreshSelectedDate()
                    },
                    onItemClick = { item ->
                        selectedListItem = item
                        navController.navigate(NavRoutes.ListItemDetail.createRoute(item.id))
                    },
                    onAddFromYourLists = {},
                    onAddItem = {},
                    viewModel = listDetailViewModel
                )
            }
        }

        /*
         * NEW LIST
         */
        composable(NavRoutes.NewList.route) {
            NewListScreen(
                onBack = {
                    navController.popBackStack()
                },
                onCreateList = { title, category ->
                    coroutineScope.launch {
                        val user = repositories.userSession.getCurrentUser()
                        val newList = DinamList(
                            id = UUID.randomUUID().toString(),
                            userId = user.id,
                            title = title,
                            category = category,
                            archivedAt = null,
                            createdAt = System.currentTimeMillis()
                        )
                        repositories.createListUseCase(newList)
                        selectedList = newList
                        selectedListItems = emptyList()
                        listDetailViewModel.loadItems(newList.id)
                        homeViewModel.loadLists()
                        navController.popBackStack()
                        navController.navigate(NavRoutes.ListDetail.createRoute(newList.id))
                    }
                }
            )
        }

        /*
         * RESCHEDULE
         */
        composable(NavRoutes.Reschedule.route) {
            val item = rescheduleItem
            if (item != null) {
                RescheduleScreen(
                    item = item,
                    onBack = {
                        navController.popBackStack()
                    },
                    onRescheduleChanged = { overdueItem, newDate, newTime, newRemindMe ->
                        navController.popBackStack()
                        todayViewModel.rescheduleOverdueItem(
                            overdueItem = overdueItem,
                            targetDate = newDate,
                            targetTime = newTime,
                            remindMe = newRemindMe
                        )
                    }
                )
            }
        }

        /*
         * ITEM DETAIL
         */
        composable(NavRoutes.ItemDetail.route) {
            val item = selectedItem
            if (item != null) {
                ItemDetailScreen(
                    itemTitle = item.text,
                    itemTime = editedItemTime,
                    everyDayEnabled = everyDayEnabled,
                    onBack = {
                        navController.popBackStack()
                    },
                    onTimeClick = {
                        showItemTimePicker = true
                    },
                    onEveryDayChanged = { enabled ->
                        val todayItem = selectedTodayItem
                        if (todayItem != null) {
                            everyDayEnabled = enabled
                            todayViewModel.setEveryday(item = todayItem, enabled = enabled)
                        }
                    },
                    onRemoveItem = {
                        if (everyDayEnabled) {
                            showScopeSheet = true
                        } else {
                            showDeleteItemConfirmDialog = true
                        }
                    }
                )
            }
        }

        /*
         * LIST ITEM DETAIL
         */
        composable(NavRoutes.ListItemDetail.route) {
            val item = selectedListItem
            if (item != null) {
                ListItemDetailScreen(
                    item = item,
                    onBack = {
                        navController.popBackStack()
                        todayViewModel.refreshSelectedDate()
                    },
                    onSaveItem = { updatedItem ->
                        selectedListItem = updatedItem
                        listDetailViewModel.updateItem(updatedItem)
                        todayViewModel.refreshSelectedDate()
                    },
                    onDeleteItem = { deletedItem ->
                        navController.popBackStack()
                        listDetailViewModel.deleteItem(deletedItem)
                        todayViewModel.refreshSelectedDate()
                    }
                )
            }
        }
    }

    /*
     * OVERLAYS (ScopeSheet, Confirm Dialog, TimePicker)
     */
    if (showScopeSheet) {
        val activeItem = itemToDelete ?: selectedItem
        if (activeItem != null) {
            ModalBottomSheet(
                onDismissRequest = {
                    showScopeSheet = false
                    itemToDelete = null
                },
                containerColor = DinamColors.Surface
            ) {
                ScopeSheet(
                    itemTitle = activeItem.text,
                    onJustToday = {
                        todayViewModel.deleteJustToday(activeItem)
                        showScopeSheet = false
                        itemToDelete = null
                        selectedItem = null
                        selectedTodayItem = null
                        editedItemTime = null
                        everyDayEnabled = false
                    },
                    onTodayAndFuture = {
                        todayViewModel.deleteTodayAndFuture(activeItem)
                        showScopeSheet = false
                        itemToDelete = null
                        selectedItem = null
                        selectedTodayItem = null
                        editedItemTime = null
                        everyDayEnabled = false
                    },
                    onCancel = {
                        showScopeSheet = false
                        itemToDelete = null
                    }
                )
            }
        }
    }

    if (showDeleteItemConfirmDialog) {
        val activeItem = itemToDelete ?: selectedItem
        if (activeItem != null) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteItemConfirmDialog = false
                    itemToDelete = null
                },
                title = {
                    Text(
                        text = "Delete item",
                        color = DinamColors.TextTitle
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete \"${activeItem.text}\"?",
                        color = DinamColors.TextPrimary
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            todayViewModel.deleteJustToday(activeItem)
                            showDeleteItemConfirmDialog = false
                            itemToDelete = null
                            selectedItem = null
                            selectedTodayItem = null
                            editedItemTime = null
                            everyDayEnabled = false
                        }
                    ) {
                        Text(text = "Delete")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteItemConfirmDialog = false
                            itemToDelete = null
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            )
        }
    }

    if (showItemTimePicker && selectedItem != null) {
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
                    val currentItem = selectedItem
                    if (currentItem != null) {
                        todayViewModel.updateItemTime(item = currentItem, newTime = time)
                        selectedItem = currentItem.copy(remindAt = time)
                    }
                },
                onClear = {
                    editedItemTime = null
                    showItemTimePicker = false
                    val currentItem = selectedItem
                    if (currentItem != null) {
                        todayViewModel.updateItemTime(item = currentItem, newTime = null)
                        selectedItem = currentItem.copy(remindAt = null)
                    }
                },
                onDismiss = {
                    showItemTimePicker = false
                }
            )
        }
    }
}