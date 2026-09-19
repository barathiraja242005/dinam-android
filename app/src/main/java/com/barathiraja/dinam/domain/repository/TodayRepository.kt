package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.TodayItem

interface TodayRepository {
    suspend fun getAllItems(): List<TodayItem>
    suspend fun getItemById(id: String): TodayItem?
    suspend fun getItemsForDate(userId: String, periodDate: String): List<TodayItem>
    suspend fun insertItem(item: TodayItem)
    suspend fun insertAll(items: List<TodayItem>)
    suspend fun updateItem(item: TodayItem)
    suspend fun deleteItem(item: TodayItem)
}