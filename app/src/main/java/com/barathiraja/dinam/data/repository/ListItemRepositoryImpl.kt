package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ListItemDao
import com.barathiraja.dinam.data.local.mapper.ListItemMapper
import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.repository.ListItemRepository

class ListItemRepositoryImpl(
    private val listItemDao: ListItemDao
) : ListItemRepository {

    override suspend fun getItemsForList(listId: String): List<ListItem> {
        return listItemDao.getByListId(listId).map { ListItemMapper.toDomain(it) }
    }

    override suspend fun getOverdueListItems(todayDate: String): List<ListItem> {
        return listItemDao.getOverdueListItems(todayDate).map { ListItemMapper.toDomain(it) }
    }

    override suspend fun getScheduledListItems(periodDate: String): List<ListItem> {
        return listItemDao.getScheduledListItems(periodDate).map { ListItemMapper.toDomain(it) }
    }

    override suspend fun insertItem(item: ListItem) {
        listItemDao.insert(ListItemMapper.toEntity(item))
    }

    override suspend fun insertItems(items: List<ListItem>) {
        listItemDao.insertAll(items.map { ListItemMapper.toEntity(it) })
    }

    override suspend fun updateItem(item: ListItem) {
        listItemDao.update(ListItemMapper.toEntity(item))
    }

    override suspend fun deleteItem(item: ListItem) {
        listItemDao.delete(ListItemMapper.toEntity(item))
    }
}