package com.barathiraja.dinam.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.data.local.entity.ListItemEntity
import com.barathiraja.dinam.data.repository.ListItemRepository
import com.barathiraja.dinam.domain.util.Canonicalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ListDetailUiState(
    val items: List<ListItemEntity> = emptyList(),
    val isLoading: Boolean = false
)

class ListDetailViewModel(
    private val listItemRepository: ListItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ListDetailUiState()
    )

    val uiState: StateFlow<ListDetailUiState> =
        _uiState.asStateFlow()

    fun loadItems(
        listId: String
    ) {
        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true
                )

            val items =
                listItemRepository.getItemsForList(
                    listId = listId
                )

            _uiState.value =
                ListDetailUiState(
                    items = items,
                    isLoading = false
                )
        }
    }

    fun setItemChecked(
        item: ListItemEntity,
        checked: Boolean
    ) {

        val updatedItem =
            item.copy(
                checked = checked
            )

        _uiState.value =
            _uiState.value.copy(
                items =
                    _uiState.value.items.map { currentItem ->
                        if (currentItem.id == item.id) {
                            updatedItem
                        } else {
                            currentItem
                        }
                    }
            )

        viewModelScope.launch {

            listItemRepository.updateItem(
                updatedItem
            )
        }
    }

    fun addItem(
        listId: String,
        text: String
    ) {

        val trimmedText =
            text.trim()

        if (trimmedText.isEmpty()) {
            return
        }

        viewModelScope.launch {

            val currentItems =
                _uiState.value.items

            val item =
                ListItemEntity(
                    id = UUID.randomUUID().toString(),
                    listId = listId,
                    text = trimmedText,
                    canonicalId =
                        Canonicalizer.canonicalId(
                            trimmedText
                        ),
                    position = currentItems.size,
                    checked = false
                )

            listItemRepository.insertItem(
                item
            )

            _uiState.value =
                _uiState.value.copy(
                    items =
                        currentItems + item
                )
        }
    }
}

class ListDetailViewModelFactory(
    private val listItemRepository: ListItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                ListDetailViewModel::class.java
            )
        ) {

            return ListDetailViewModel(
                listItemRepository =
                    listItemRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}