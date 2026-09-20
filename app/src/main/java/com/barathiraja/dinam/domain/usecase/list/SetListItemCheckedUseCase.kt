package com.barathiraja.dinam.domain.usecase.list

import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.repository.ListItemRepository

class SetListItemCheckedUseCase(
    private val listItemRepository: ListItemRepository
) {
    suspend operator fun invoke(item: ListItem, checked: Boolean) {
        listItemRepository.updateItem(item.copy(checked = checked))
    }
}