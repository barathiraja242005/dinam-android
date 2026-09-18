package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ItemStatDao
import com.barathiraja.dinam.data.local.entity.ItemStatEntity

class ItemStatRepository(
    private val itemStatDao: ItemStatDao
) {

    suspend fun getByCategory(
        userId: String,
        category: String
    ): List<ItemStatEntity> {
        return itemStatDao.getByCategory(
            userId = userId,
            category = category
        )
    }

    suspend fun getByCanonicalId(
        userId: String,
        category: String,
        canonicalId: String
    ): ItemStatEntity? {
        return itemStatDao.getByCanonicalId(
            userId = userId,
            category = category,
            canonicalId = canonicalId
        )
    }

    suspend fun insert(
        itemStat: ItemStatEntity
    ) {
        itemStatDao.insert(itemStat)
    }

    suspend fun update(
        itemStat: ItemStatEntity
    ) {
        itemStatDao.update(itemStat)
    }
}