package com.barathiraja.dinam.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.data.local.entity.ListEntity
import com.barathiraja.dinam.data.repository.ListRepository
import com.barathiraja.dinam.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class HomeUiState(
    val lists: List<ListEntity> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val listRepository: ListRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState()
    )

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        loadLists()
    }

    fun loadLists() {
        viewModelScope.launch {

            _uiState.value = HomeUiState(
                lists = emptyList(),
                isLoading = true
            )

            val user = getOrCreateUser()

            val lists =
                listRepository.getActiveLists(
                    userId = user.id
                )

            _uiState.value = HomeUiState(
                lists = lists,
                isLoading = false
            )
        }
    }

    private suspend fun getOrCreateUser(): com.barathiraja.dinam.data.local.entity.UserEntity {

        val existingUser =
            userRepository.getUser()

        if (existingUser != null) {
            return existingUser
        }

        val newUser =
            com.barathiraja.dinam.data.local.entity.UserEntity(
                id = UUID.randomUUID().toString()
            )

        userRepository.createUser(
            newUser
        )

        return newUser
    }
}

class HomeViewModelFactory(
    private val listRepository: ListRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                HomeViewModel::class.java
            )
        ) {

            return HomeViewModel(
                listRepository = listRepository,
                userRepository = userRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}