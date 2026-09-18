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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class TodayUiState(
    val items: List<OccurrenceItemEntity> = emptyList(),
    val isLoading: Boolean = true
)

class TodayViewModel(
    private val todayOccurrenceService: TodayOccurrenceService,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayUiState())
    val uiState: StateFlow<TodayUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            val user = getOrCreateUser()
            val periodDate = currentLocalDate()

            val occurrence = todayOccurrenceService.getOrCreateOccurrence(
                userId = user.id,
                periodDate = periodDate
            )

            val items = todayOccurrenceService.getOccurrenceItems(
                occurrenceId = occurrence.id
            )

            _uiState.value = TodayUiState(
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
            val periodDate = currentLocalDate()

            val item = TodayItemEntity(
                id = UUID.randomUUID().toString(),
                userId = user.id,
                text = trimmedText,
                canonicalId = canonicalId(trimmedText),
                remindAt = remindAt,
                skipIfComplete = false,
                position = _uiState.value.items.size,
                activeFrom = periodDate,
                activeUntil = null
            )

            todayOccurrenceService.addTodayItem(
                userId = user.id,
                periodDate = periodDate,
                item = item
            )

            loadItems()
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

            loadItems()
        }
    }

    private suspend fun getOrCreateUser(): UserEntity {
        val existingUser = userRepository.getUser()

        if (existingUser != null) {
            return existingUser
        }

        val newUser = UserEntity(
            id = UUID.randomUUID().toString()
        )

        userRepository.createUser(newUser)

        return newUser
    }

    private fun currentLocalDate(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.US
        ).format(Date())
    }

    private fun canonicalId(text: String): String {
        val normalized = text
            .lowercase(Locale.US)
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        val digest = MessageDigest.getInstance("SHA-256")
            .digest(normalized.toByteArray())

        return digest.joinToString("") {
            "%02x".format(it)
        }
    }
}

class TodayViewModelFactory(
    private val todayOccurrenceService: TodayOccurrenceService,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(TodayViewModel::class.java)) {
            return TodayViewModel(
                todayOccurrenceService = todayOccurrenceService,
                userRepository = userRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}