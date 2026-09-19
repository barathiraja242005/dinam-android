package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.OccurrenceDao
import com.barathiraja.dinam.data.local.entity.OccurrenceEntity
import com.barathiraja.dinam.domain.model.Occurrence
import com.barathiraja.dinam.domain.repository.OccurrenceRepository

class OccurrenceRepositoryImpl(
    private val occurrenceDao: OccurrenceDao
) : OccurrenceRepository {

    override suspend fun getByPeriodDate(userId: String, periodDate: String): Occurrence? {
        return occurrenceDao.getByDate(userId, periodDate)?.toDomain()
    }

    override suspend fun getOccurrencesFromDate(userId: String, fromDate: String): List<Occurrence> {
        return occurrenceDao.getFromDate(userId, fromDate).map { it.toDomain() }
    }

    override suspend fun getById(id: String): Occurrence? {
        return occurrenceDao.getById(id)?.toDomain()
    }

    override suspend fun insert(occurrence: Occurrence) {
        occurrenceDao.insert(occurrence.toEntity())
    }

    override suspend fun delete(occurrence: Occurrence) {
        occurrenceDao.delete(occurrence.toEntity())
    }

    private fun OccurrenceEntity.toDomain(): Occurrence {
        return Occurrence(
            id = id,
            userId = userId,
            periodDate = periodDate,
            createdAt = createdAt
        )
    }

    private fun Occurrence.toEntity(): OccurrenceEntity {
        return OccurrenceEntity(
            id = id,
            userId = userId,
            periodDate = periodDate,
            createdAt = createdAt
        )
    }
}