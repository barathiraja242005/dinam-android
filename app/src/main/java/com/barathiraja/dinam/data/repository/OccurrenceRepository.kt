package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.OccurrenceDao
import com.barathiraja.dinam.data.local.entity.OccurrenceEntity

class OccurrenceRepository(
    private val occurrenceDao: OccurrenceDao
) {

    suspend fun getOccurrenceForDate(
        userId: String,
        periodDate: String
    ): OccurrenceEntity? {
        return occurrenceDao.getByDate(
            userId = userId,
            periodDate = periodDate
        )
    }

    suspend fun getOccurrencesFromDate(
        userId: String,
        fromDate: String
    ): List<OccurrenceEntity> {
        return occurrenceDao.getFromDate(
            userId = userId,
            fromDate = fromDate
        )
    }

    suspend fun createOccurrence(
        occurrence: OccurrenceEntity
    ) {
        occurrenceDao.insert(occurrence)
    }
}