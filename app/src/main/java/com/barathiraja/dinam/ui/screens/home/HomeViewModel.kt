package com.barathiraja.dinam.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.data.local.entity.ListEntity
import com.barathiraja.dinam.data.repository.ListRepository
import com.barathiraja.dinam.data.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val lists: List<ListEntity> = emptyList(),
    val isLoading: Boolean = true
)

class HomeViewModel(
    private val listRepository: ListRepository,
    private val userSession: UserSession
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

            val user = userSession.getCurrentUser()

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
}

class HomeViewModelFactory(
    private val listRepository: ListRepository,
    private val userSession: UserSession
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
                userSession = userSession
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}