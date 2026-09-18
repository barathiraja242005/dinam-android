package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity

@Dao
interface OccurrenceItemDao {

    @Query(
        """
        SELECT * FROM occurrence_item
        WHERE occurrence_id = :occurrenceId
        ORDER BY position ASC
        """
    )
    suspend fun getByOccurrenceId(
        occurrenceId: String
    ): List<OccurrenceItemEntity>

    @Insert
    suspend fun insertAll(
        items: List<OccurrenceItemEntity>
    )

    @Update
    suspend fun update(
        item: OccurrenceItemEntity
    )
}