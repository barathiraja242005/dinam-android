package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.TodayItemDao
import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.domain.repository.TodayRepository

class TodayRepositoryImpl(
    private val todayItemDao: TodayItemDao
) : TodayRepository {

    override suspend fun getAllItems(): List<TodayItem> {
        return todayItemDao.getAll().map { it.toDomain() }
    }

    override suspend fun getItemById(id: String): TodayItem? {
        return todayItemDao.getById(id)?.toDomain()
    }

    override suspend fun getItemsForDate(userId: String, periodDate: String): List<TodayItem> {
        return todayItemDao.getActiveItemsForDate(userId, periodDate).map { it.toDomain() }
    }

    override suspend fun insertItem(item: TodayItem) {
        todayItemDao.insert(item.toEntity())
    }

    override suspend fun insertAll(items: List<TodayItem>) {
        todayItemDao.insertAll(items.map { it.toEntity() })
    }

    override suspend fun updateItem(item: TodayItem) {
        todayItemDao.update(item.toEntity())
    }

    override suspend fun deleteItem(item: TodayItem) {
        todayItemDao.delete(item.toEntity())
    }

    private fun TodayItemEntity.toDomain(): TodayItem {
        return TodayItem(
            id = id,
            userId = userId,
            text = text,
            canonicalId = canonicalId,
            remindAt = remindAt,
            skipIfComplete = skipIfComplete,
            position = position,
            activeFrom = activeFrom,
            activeUntil = activeUntil
        )
    }

    private fun TodayItem.toEntity(): TodayItemEntity {
        return TodayItemEntity(
            id = id,
            userId = userId,
            text = text,
            canonicalId = canonicalId,
            remindAt = remindAt,
            skipIfComplete = skipIfComplete,
            position = position,
            activeFrom = activeFrom,
            activeUntil = activeUntil
        )
    }
}