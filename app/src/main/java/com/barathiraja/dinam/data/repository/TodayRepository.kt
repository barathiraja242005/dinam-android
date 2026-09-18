package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.TodayItemDao
import com.barathiraja.dinam.data.local.entity.TodayItemEntity

class TodayRepository(
    private val todayItemDao: TodayItemDao
) {

    suspend fun getItemsForDate(
        userId: String,
        periodDate: String
    ): List<TodayItemEntity> {
        return todayItemDao.getActiveItemsForDate(
            userId = userId,
            periodDate = periodDate
        )
    }

    suspend fun insertItem(
        item: TodayItemEntity
    ) {
        todayItemDao.insert(item)
    }

    suspend fun updateItem(
        item: TodayItemEntity
    ) {
        todayItemDao.update(item)
    }

    suspend fun deleteItem(
        item: TodayItemEntity
    ) {
        todayItemDao.delete(item)
    }
}