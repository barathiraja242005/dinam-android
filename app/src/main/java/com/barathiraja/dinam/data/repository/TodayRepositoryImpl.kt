package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.TodayItemDao
import com.barathiraja.dinam.data.local.mapper.TodayItemMapper
import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.domain.repository.TodayRepository

class TodayRepositoryImpl(
    private val todayItemDao: TodayItemDao
) : TodayRepository {

    override suspend fun getAllItems(): List<TodayItem> {
        return todayItemDao.getAll().map { TodayItemMapper.toDomain(it) }
    }

    override suspend fun getItemById(id: String): TodayItem? {
        return todayItemDao.getById(id)?.let { TodayItemMapper.toDomain(it) }
    }

    override suspend fun getItemsForDate(userId: String, periodDate: String): List<TodayItem> {
        return todayItemDao.getActiveItemsForDate(userId, periodDate).map { TodayItemMapper.toDomain(it) }
    }

    override suspend fun insertItem(item: TodayItem) {
        todayItemDao.insert(TodayItemMapper.toEntity(item))
    }

    override suspend fun insertAll(items: List<TodayItem>) {
        todayItemDao.insertAll(items.map { TodayItemMapper.toEntity(it) })
    }

    override suspend fun updateItem(item: TodayItem) {
        todayItemDao.update(TodayItemMapper.toEntity(item))
    }

    override suspend fun deleteItem(item: TodayItem) {
        todayItemDao.delete(TodayItemMapper.toEntity(item))
    }
}