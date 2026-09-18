package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ListItemDao
import com.barathiraja.dinam.data.local.entity.ListItemEntity

class ListItemRepository(
    private val listItemDao: ListItemDao
) {

    suspend fun getItemsForList(
        listId: String
    ): List<ListItemEntity> {
        return listItemDao.getByListId(
            listId = listId
        )
    }

    suspend fun insertItem(
        item: ListItemEntity
    ) {
        listItemDao.insert(item)
    }

    suspend fun insertItems(
        items: List<ListItemEntity>
    ) {
        listItemDao.insertAll(items)
    }

    suspend fun updateItem(
        item: ListItemEntity
    ) {
        listItemDao.update(item)
    }
}