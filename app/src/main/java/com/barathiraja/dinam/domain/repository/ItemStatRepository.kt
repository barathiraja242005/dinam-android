package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.ItemStat

interface ItemStatRepository {
    suspend fun getByCategory(userId: String, category: String): List<ItemStat>
    suspend fun getStatByCanonicalId(userId: String, category: String, canonicalId: String): ItemStat?
    suspend fun insertOrUpdateStat(stat: ItemStat)
}