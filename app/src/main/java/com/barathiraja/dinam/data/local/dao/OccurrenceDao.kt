package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.barathiraja.dinam.data.local.entity.OccurrenceEntity

@Dao
interface OccurrenceDao {

    @Query(
        """
        SELECT * FROM occurrence
        WHERE user_id = :userId
          AND period_date = :periodDate
        LIMIT 1
        """
    )
    suspend fun getByDate(
        userId: String,
        periodDate: String
    ): OccurrenceEntity?

    @Query(
        """
        SELECT * FROM occurrence
        WHERE user_id = :userId
          AND period_date >= :fromDate
        ORDER BY period_date ASC
        """
    )
    suspend fun getFromDate(
        userId: String,
        fromDate: String
    ): List<OccurrenceEntity>

    @Insert
    suspend fun insert(
        occurrence: OccurrenceEntity
    )
}