package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM occurrence WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): OccurrenceEntity?

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

    @Query(
        """
        SELECT * FROM occurrence
        WHERE user_id = :userId
          AND period_date < :todayDate
        ORDER BY period_date DESC
        """
    )
    suspend fun getPastOccurrences(
        userId: String,
        todayDate: String
    ): List<OccurrenceEntity>

    @Insert
    suspend fun insert(
        occurrence: OccurrenceEntity
    )

    @Delete
    suspend fun delete(
        occurrence: OccurrenceEntity
    )
}