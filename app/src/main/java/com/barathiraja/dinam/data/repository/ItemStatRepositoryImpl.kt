package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ItemStatDao
import com.barathiraja.dinam.data.local.entity.ItemStatEntity
import com.barathiraja.dinam.domain.model.ItemStat
import com.barathiraja.dinam.domain.repository.ItemStatRepository

class ItemStatRepositoryImpl(
    private val itemStatDao: ItemStatDao
) : ItemStatRepository {

    override suspend fun getByCategory(userId: String, category: String): List<ItemStat> {
        return itemStatDao.getByCategory(userId, category).map { it.toDomain() }
    }

    override suspend fun getStatByCanonicalId(userId: String, category: String, canonicalId: String): ItemStat? {
        return itemStatDao.getByCanonicalId(userId, category, canonicalId)?.toDomain()
    }

    override suspend fun insertOrUpdateStat(stat: ItemStat) {
        val existing = itemStatDao.getByCanonicalId(stat.userId, stat.category, stat.canonicalId)
        if (existing != null) {
            itemStatDao.update(stat.toEntity())
        } else {
            itemStatDao.insert(stat.toEntity())
        }
    }

    private fun ItemStatEntity.toDomain(): ItemStat {
        return ItemStat(
            userId = userId,
            category = category,
            canonicalId = canonicalId,
            displayText = displayText,
            useCount = useCount,
            lastUsedAt = lastUsedAt,
            avgIntervalDays = avgIntervalDays
        )
    }

    private fun ItemStat.toEntity(): ItemStatEntity {
        return ItemStatEntity(
            userId = userId,
            category = category,
            canonicalId = canonicalId,
            displayText = displayText,
            useCount = useCount,
            lastUsedAt = lastUsedAt,
            avgIntervalDays = avgIntervalDays
        )
    }
}