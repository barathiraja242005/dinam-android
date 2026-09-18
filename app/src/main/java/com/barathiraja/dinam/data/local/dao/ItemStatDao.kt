package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.barathiraja.dinam.data.local.entity.ItemStatEntity

@Dao
interface ItemStatDao {

    @Query(
        """
        SELECT * FROM item_stat
        WHERE user_id = :userId
          AND category = :category
        ORDER BY use_count DESC, last_used_at DESC
        """
    )
    suspend fun getByCategory(
        userId: String,
        category: String
    ): List<ItemStatEntity>

    @Query(
        """
        SELECT * FROM item_stat
        WHERE user_id = :userId
          AND category = :category
          AND canonical_id = :canonicalId
        LIMIT 1
        """
    )
    suspend fun getByCanonicalId(
        userId: String,
        category: String,
        canonicalId: String
    ): ItemStatEntity?

    @Insert
    suspend fun insert(
        itemStat: ItemStatEntity
    )

    @Update
    suspend fun update(
        itemStat: ItemStatEntity
    )
}