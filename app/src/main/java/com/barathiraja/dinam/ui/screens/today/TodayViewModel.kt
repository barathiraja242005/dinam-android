package com.barathiraja.dinam.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.data.service.TodayOccurrenceService
import com.barathiraja.dinam.data.session.UserSession
import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.TodayItem
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
    val isLoading: Boolean = true
)

class TodayViewModel(
    private val todayOccurrenceService: TodayOccurrenceService,
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
                isLoading = true
            )

            val user = userSession.getCurrentUser()

            val occurrence =
                todayOccurrenceService.getOrCreateOccurrence(
                    userId = user.id,
                    periodDate = periodDate
                )

            val items =
                todayOccurrenceService.getOccurrenceItems(
                    occurrenceId = occurrence.id
                )

            _uiState.value = TodayUiState(
                selectedDate = periodDate,
                items = items,
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

            todayOccurrenceService.addTodayItem(
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

            todayOccurrenceService.setOccurrenceItemChecked(
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
                }
            )
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
    private val todayOccurrenceService: TodayOccurrenceService,
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
                todayOccurrenceService =
                    todayOccurrenceService,
                userSession =
                    userSession
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}