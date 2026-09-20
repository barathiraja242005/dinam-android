package com.barathiraja.dinam.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.repository.ListItemRepository
import com.barathiraja.dinam.domain.usecase.list.AddListItemUseCase
import com.barathiraja.dinam.domain.usecase.list.DeleteListItemUseCase
import com.barathiraja.dinam.domain.usecase.list.RenameListItemUseCase
import com.barathiraja.dinam.domain.usecase.list.SetListItemCheckedUseCase
import com.barathiraja.dinam.domain.util.Canonicalizer
import com.barathiraja.dinam.domain.util.IdGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ListDetailUiState(
    val items: List<ListItem> = emptyList(),
    val isLoading: Boolean = false
)

class ListDetailViewModel(
    private val listItemRepository: ListItemRepository,
    private val addListItemUseCase: AddListItemUseCase,
    private val renameListItemUseCase: RenameListItemUseCase,
    private val deleteListItemUseCase: DeleteListItemUseCase,
    private val setListItemCheckedUseCase: SetListItemCheckedUseCase,
    private val idGenerator: IdGenerator = IdGenerator.Default
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
        item: ListItem,
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
            setListItemCheckedUseCase(
                item = item,
                checked = checked
            )
        }
    }

    fun updateItem(item: ListItem) {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.map { currentItem ->
                if (currentItem.id == item.id) {
                    item
                } else {
                    currentItem
                }
            }
        )
        viewModelScope.launch {
            renameListItemUseCase(
                item = item,
                newText = item.text,
                canonicalId = item.canonicalId
            )
        }
    }

    fun deleteItem(item: ListItem) {
        _uiState.value = _uiState.value.copy(
            items = _uiState.value.items.filterNot { it.id == item.id }
        )
        viewModelScope.launch {
            deleteListItemUseCase(item)
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
                ListItem(
                    id = idGenerator.generateId(),
                    listId = listId,
                    text = trimmedText,
                    canonicalId =
                        Canonicalizer.canonicalId(
                            trimmedText
                        ),
                    position = currentItems.size,
                    checked = false
                )

            addListItemUseCase(
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
    private val listItemRepository: ListItemRepository,
    private val addListItemUseCase: AddListItemUseCase,
    private val renameListItemUseCase: RenameListItemUseCase,
    private val deleteListItemUseCase: DeleteListItemUseCase,
    private val setListItemCheckedUseCase: SetListItemCheckedUseCase
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
                listItemRepository = listItemRepository,
                addListItemUseCase = addListItemUseCase,
                renameListItemUseCase = renameListItemUseCase,
                deleteListItemUseCase = deleteListItemUseCase,
                setListItemCheckedUseCase = setListItemCheckedUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}