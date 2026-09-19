package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.ListItem

interface ListItemRepository {
    suspend fun getItemsForList(listId: String): List<ListItem>
    suspend fun insertItem(item: ListItem)
    suspend fun insertItems(items: List<ListItem>)
    suspend fun updateItem(item: ListItem)
    suspend fun deleteItem(item: ListItem)
}