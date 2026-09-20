package com.barathiraja.dinam.domain.usecase.list

import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.repository.ListItemRepository

class RenameListItemUseCase(
    private val listItemRepository: ListItemRepository
) {
    suspend operator fun invoke(item: ListItem, newText: String, canonicalId: String) {
        listItemRepository.updateItem(item.copy(text = newText, canonicalId = canonicalId))
    }
}