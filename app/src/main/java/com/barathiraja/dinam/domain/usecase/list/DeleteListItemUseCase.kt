package com.barathiraja.dinam.domain.usecase.list

import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.repository.ListItemRepository

class DeleteListItemUseCase(
    private val listItemRepository: ListItemRepository
) {
    suspend operator fun invoke(item: ListItem) {
        listItemRepository.deleteItem(item)
    }
}