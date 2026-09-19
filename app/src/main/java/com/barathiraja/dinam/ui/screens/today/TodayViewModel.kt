package com.barathiraja.dinam.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.data.local.entity.UserEntity
import com.barathiraja.dinam.data.repository.UserRepository
import com.barathiraja.dinam.data.service.TodayOccurrenceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.time.LocalDate
import java.util.Locale
import java.util.UUID

data class TodayUiState(
    val selectedDate: String = currentLocalDate(),
    val items: List<OccurrenceItemEntity> = emptyList(),
    val isLoading: Boolean = true
)

class TodayViewModel(
    private val todayOccurrenceService: TodayOccurrenceService,
    private val userRepository: UserRepository
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

    /*
     * Explicitly reloads the currently selected date.
     *
     * This is different from selectDate(), because selectDate()
     * intentionally does nothing when the same date is selected.
     *
     * We use this after an action changes the occurrence without
     * changing the selected date, such as "Just today".
     */
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

            val user = getOrCreateUser()

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
        remindAt: String?
    ) {

        val trimmedText = text.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        viewModelScope.launch {

            val user = getOrCreateUser()

            val periodDate =
                _uiState.value.selectedDate

            /*
             * New items are one-day items by default.
             *
             * Every day is therefore OFF until the user
             * explicitly enables it from Item Detail.
             */
            val item = TodayItemEntity(
                id = UUID.randomUUID().toString(),
                userId = user.id,
                text = trimmedText,
                canonicalId = canonicalId(
                    trimmedText
                ),
                remindAt = remindAt,
                skipIfComplete = false,
                position = _uiState.value.items.size,
                activeFrom = periodDate,
                activeUntil = periodDate
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
        item: OccurrenceItemEntity,
        checked: Boolean
    ) {
        viewModelScope.launch {

            todayOccurrenceService.setOccurrenceItemChecked(
                item = item,
                checked = checked
            )

            /*
             * Update only the item that was checked.
             *
             * We intentionally do NOT reload the occurrence
             * here. This prevents the whole page from visibly
             * refreshing when the checkbox is tapped.
             */
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

    private suspend fun getOrCreateUser(): UserEntity {

        val existingUser =
            userRepository.getUser()

        if (existingUser != null) {
            return existingUser
        }

        val newUser = UserEntity(
            id = UUID.randomUUID().toString()
        )

        userRepository.createUser(
            newUser
        )

        return newUser
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

    companion object {

        fun currentLocalDate(): String {
            return LocalDate.now().toString()
        }
    }
}

private fun currentLocalDate(): String {
    return TodayViewModel.currentLocalDate()
}

class TodayViewModelFactory(
    private val todayOccurrenceService: TodayOccurrenceService,
    private val userRepository: UserRepository
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
                userRepository =
                    userRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}