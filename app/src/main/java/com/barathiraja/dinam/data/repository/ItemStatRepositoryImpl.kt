package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ItemStatDao
import com.barathiraja.dinam.data.local.mapper.ItemStatMapper
import com.barathiraja.dinam.domain.model.ItemStat
import com.barathiraja.dinam.domain.repository.ItemStatRepository

class ItemStatRepositoryImpl(
    private val itemStatDao: ItemStatDao
) : ItemStatRepository {

    override suspend fun getByCategory(userId: String, category: String): List<ItemStat> {
        return itemStatDao.getByCategory(userId, category).map { ItemStatMapper.toDomain(it) }
    }

    override suspend fun getStatByCanonicalId(userId: String, category: String, canonicalId: String): ItemStat? {
        return itemStatDao.getByCanonicalId(userId, category, canonicalId)?.let { ItemStatMapper.toDomain(it) }
    }

    override suspend fun insertOrUpdateStat(stat: ItemStat) {
        val existing = itemStatDao.getByCanonicalId(stat.userId, stat.category, stat.canonicalId)
        if (existing != null) {
            itemStatDao.update(ItemStatMapper.toEntity(stat))
        } else {
            itemStatDao.insert(ItemStatMapper.toEntity(stat))
        }
    }
}