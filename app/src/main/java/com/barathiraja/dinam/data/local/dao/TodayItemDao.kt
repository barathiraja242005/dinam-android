package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.barathiraja.dinam.data.local.entity.TodayItemEntity

@Dao
interface TodayItemDao {

    @Query(
        """
        SELECT * FROM today_item
        WHERE user_id = :userId
          AND active_from <= :periodDate
          AND (
              active_until IS NULL
              OR active_until >= :periodDate
          )
        ORDER BY
            CASE WHEN remind_at IS NULL THEN 1 ELSE 0 END,
            remind_at ASC,
            position ASC
        """
    )
    suspend fun getActiveItemsForDate(
        userId: String,
        periodDate: String
    ): List<TodayItemEntity>

    @Query("SELECT * FROM today_item WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): TodayItemEntity?

    @Query("SELECT * FROM today_item")
    suspend fun getAll(): List<TodayItemEntity>

    @Insert
    suspend fun insert(
        item: TodayItemEntity
    )

    @Insert
    suspend fun insertAll(
        items: List<TodayItemEntity>
    )

    @Update
    suspend fun update(
        item: TodayItemEntity
    )

    @Delete
    suspend fun delete(
        item: TodayItemEntity
    )
}