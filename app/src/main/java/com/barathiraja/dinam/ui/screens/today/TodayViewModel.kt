package com.barathiraja.dinam.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.data.session.UserSession
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.OverdueItem
import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.domain.usecase.occurrence.GetOccurrenceUseCase
import com.barathiraja.dinam.domain.usecase.occurrence.RescheduleOverdueItemUseCase
import com.barathiraja.dinam.domain.usecase.today.AddTodayItemUseCase
import com.barathiraja.dinam.domain.usecase.today.DeleteTodayItemUseCase
import com.barathiraja.dinam.domain.usecase.today.RenameTodayItemUseCase
import com.barathiraja.dinam.domain.usecase.today.SetTodayItemCheckedUseCase
import com.barathiraja.dinam.domain.usecase.today.SetTodayItemEverydayUseCase
import com.barathiraja.dinam.domain.usecase.today.UpdateTodayItemTimeUseCase
import com.barathiraja.dinam.domain.util.Canonicalizer
import com.barathiraja.dinam.domain.util.DateProvider
import com.barathiraja.dinam.domain.util.IdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TodayUiState(
    val selectedDate: String = currentLocalDate(),
    val items: List<OccurrenceItem> = emptyList(),
    val overdueItems: List<OverdueItem> = emptyList(),
    val isLoading: Boolean = true
)

class TodayViewModel(
    private val getOccurrenceUseCase: GetOccurrenceUseCase,
    private val addTodayItemUseCase: AddTodayItemUseCase,
    private val renameTodayItemUseCase: RenameTodayItemUseCase,
    private val updateTodayItemTimeUseCase: UpdateTodayItemTimeUseCase,
    private val setTodayItemCheckedUseCase: SetTodayItemCheckedUseCase,
    private val deleteTodayItemUseCase: DeleteTodayItemUseCase,
    private val setTodayItemEverydayUseCase: SetTodayItemEverydayUseCase,
    private val rescheduleOverdueItemUseCase: RescheduleOverdueItemUseCase,
    private val userSession: UserSession,
    private val idGenerator: IdGenerator = IdGenerator.Default
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TodayUiState()
    )

    val uiState: StateFlow<TodayUiState> =
        _uiState.asStateFlow()

    init {
        loadItemsForDate(
            periodDate = currentLocalDate()
        )
    }

    fun selectDate(
        periodDate: String
    ) {
        if (periodDate == _uiState.value.selectedDate) {
            return
        }

        loadItemsForDate(
            periodDate = periodDate
        )
    }

    fun refreshSelectedDate() {
        loadItemsForDate(
            periodDate = _uiState.value.selectedDate
        )
    }

    private fun loadItemsForDate(
        periodDate: String
    ) {
        viewModelScope.launch {

            _uiState.value = TodayUiState(
                selectedDate = periodDate,
                items = emptyList(),
                overdueItems = emptyList(),
                isLoading = true
            )

            val user = userSession.getCurrentUser()

            val items =
                getOccurrenceUseCase.getOccurrenceItems(
                    occurrenceId = user.id,
                    periodDate = periodDate
                )

            val overdueItems =
                if (periodDate == currentLocalDate()) {
                    getOccurrenceUseCase.getOverdueItems(
                        userId = user.id,
                        todayDate = currentLocalDate()
                    )
                } else {
                    emptyList()
                }

            _uiState.value = TodayUiState(
                selectedDate = periodDate,
                items = items,
                overdueItems = overdueItems,
                isLoading = false
            )
        }
    }

    fun addItem(
        text: String,
        remindAt: String?,
        everyday: Boolean = false
    ) {

        val trimmedText = text.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        viewModelScope.launch {

            val user = userSession.getCurrentUser()

            val periodDate =
                _uiState.value.selectedDate

            val item = TodayItem(
                id = idGenerator.generateId(),
                userId = user.id,
                text = trimmedText,
                canonicalId =
                    Canonicalizer.canonicalId(
                        trimmedText
                    ),
                remindAt = remindAt,
                skipIfComplete = false,
                position = _uiState.value.items.size,
                activeFrom = periodDate,
                activeUntil = if (everyday) null else periodDate
            )

            addTodayItemUseCase(
                userId = user.id,
                periodDate = periodDate,
                item = item
            )

            loadItemsForDate(
                periodDate = periodDate
            )
        }
    }

    fun setItemChecked(
        item: OccurrenceItem,
        checked: Boolean
    ) {
        viewModelScope.launch {

            setTodayItemCheckedUseCase(
                item = item,
                checked = checked
            )

            val updatedItem = item.copy(
                checked = checked,
                checkedAt = if (checked) {
                    System.currentTimeMillis()
                } else {
                    null
                }
            )

            _uiState.value = _uiState.value.copy(
                items = _uiState.value.items.map { currentItem ->
                    if (currentItem.id == item.id) {
                        updatedItem
                    } else {
                        currentItem
                    }
                },
                overdueItems = _uiState.value.overdueItems.filterNot {
                    it.item.id == item.id
                }
            )
        }
    }

    fun renameItem(
        item: OccurrenceItem,
        newText: String
    ) {
        viewModelScope.launch {
            val user = userSession.getCurrentUser()
            renameTodayItemUseCase(
                userId = user.id,
                item = item,
                newText = newText,
                currentDate = _uiState.value.selectedDate
            )
            refreshSelectedDate()
        }
    }

    fun updateItemTime(
        item: OccurrenceItem,
        newTime: String?
    ) {
        viewModelScope.launch {
            updateTodayItemTimeUseCase(
                item = item,
                newTime = newTime
            )
            refreshSelectedDate()
        }
    }

    fun deleteJustToday(
        item: OccurrenceItem
    ) {
        viewModelScope.launch {
            val user = userSession.getCurrentUser()
            deleteTodayItemUseCase.removeJustToday(
                userId = user.id,
                todayItemId = item.todayItemId ?: item.id,
                periodDate = _uiState.value.selectedDate
            )
            refreshSelectedDate()
        }
    }

    fun deleteTodayAndFuture(
        item: OccurrenceItem
    ) {
        viewModelScope.launch {
            val user = userSession.getCurrentUser()
            deleteTodayItemUseCase.removeTodayAndFuture(
                userId = user.id,
                todayItemId = item.todayItemId ?: item.id,
                periodDate = _uiState.value.selectedDate
            )
            refreshSelectedDate()
        }
    }

    fun setEveryday(
        item: TodayItem,
        enabled: Boolean
    ) {
        viewModelScope.launch {
            val user = userSession.getCurrentUser()
            setTodayItemEverydayUseCase(
                userId = user.id,
                item = item,
                enabled = enabled,
                periodDate = _uiState.value.selectedDate
            )
            refreshSelectedDate()
        }
    }

    fun rescheduleOverdueItem(
        overdueItem: OccurrenceItem,
        targetDate: String = currentLocalDate(),
        targetTime: String? = overdueItem.remindAt,
        remindMe: Boolean = false
    ) {
        viewModelScope.launch {
            val user = userSession.getCurrentUser()
            rescheduleOverdueItemUseCase(
                userId = user.id,
                overdueItem = overdueItem,
                targetDate = targetDate,
                targetTime = targetTime,
                remindMe = remindMe
            )
            refreshSelectedDate()
        }
    }

    companion object {

        fun currentLocalDate(): String {
            return DateProvider.todayString()
        }
    }
}

private fun currentLocalDate(): String {
    return TodayViewModel.currentLocalDate()
}

class TodayViewModelFactory(
    private val getOccurrenceUseCase: GetOccurrenceUseCase,
    private val addTodayItemUseCase: AddTodayItemUseCase,
    private val renameTodayItemUseCase: RenameTodayItemUseCase,
    private val updateTodayItemTimeUseCase: UpdateTodayItemTimeUseCase,
    private val setTodayItemCheckedUseCase: SetTodayItemCheckedUseCase,
    private val deleteTodayItemUseCase: DeleteTodayItemUseCase,
    private val setTodayItemEverydayUseCase: SetTodayItemEverydayUseCase,
    private val rescheduleOverdueItemUseCase: RescheduleOverdueItemUseCase,
    private val userSession: UserSession
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                TodayViewModel::class.java
            )
        ) {

            return TodayViewModel(
                getOccurrenceUseCase = getOccurrenceUseCase,
                addTodayItemUseCase = addTodayItemUseCase,
                renameTodayItemUseCase = renameTodayItemUseCase,
                updateTodayItemTimeUseCase = updateTodayItemTimeUseCase,
                setTodayItemCheckedUseCase = setTodayItemCheckedUseCase,
                deleteTodayItemUseCase = deleteTodayItemUseCase,
                setTodayItemEverydayUseCase = setTodayItemEverydayUseCase,
                rescheduleOverdueItemUseCase = rescheduleOverdueItemUseCase,
                userSession = userSession
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}