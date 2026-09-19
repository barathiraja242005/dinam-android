package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ListItemDao
import com.barathiraja.dinam.data.local.entity.ListItemEntity
import com.barathiraja.dinam.domain.model.ListItem
import com.barathiraja.dinam.domain.repository.ListItemRepository

class ListItemRepositoryImpl(
    private val listItemDao: ListItemDao
) : ListItemRepository {

    override suspend fun getItemsForList(listId: String): List<ListItem> {
        return listItemDao.getByListId(listId).map { it.toDomain() }
    }

    override suspend fun getOverdueListItems(todayDate: String): List<ListItem> {
        return listItemDao.getOverdueListItems(todayDate).map { it.toDomain() }
    }

    override suspend fun getScheduledListItems(periodDate: String): List<ListItem> {
        return listItemDao.getScheduledListItems(periodDate).map { it.toDomain() }
    }

    override suspend fun insertItem(item: ListItem) {
        listItemDao.insert(item.toEntity())
    }

    override suspend fun insertItems(items: List<ListItem>) {
        listItemDao.insertAll(items.map { it.toEntity() })
    }

    override suspend fun updateItem(item: ListItem) {
        listItemDao.update(item.toEntity())
    }

    override suspend fun deleteItem(item: ListItem) {
        listItemDao.delete(item.toEntity())
    }

    private fun ListItemEntity.toDomain(): ListItem {
        return ListItem(
            id = id,
            listId = listId,
            text = text,
            canonicalId = canonicalId,
            position = position,
            checked = checked,
            dueDate = dueDate,
            remindAt = remindAt,
            remindMe = remindMe,
            snoozedUntil = snoozedUntil
        )
    }

    private fun ListItem.toEntity(): ListItemEntity {
        return ListItemEntity(
            id = id,
            listId = listId,
            text = text,
            canonicalId = canonicalId,
            position = position,
            checked = checked,
            dueDate = dueDate,
            remindAt = remindAt,
            remindMe = remindMe,
            snoozedUntil = snoozedUntil
        )
    }
}