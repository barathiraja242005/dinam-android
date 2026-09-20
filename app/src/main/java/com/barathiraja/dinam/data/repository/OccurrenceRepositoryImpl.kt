package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.OccurrenceDao
import com.barathiraja.dinam.data.local.mapper.OccurrenceMapper
import com.barathiraja.dinam.domain.model.Occurrence
import com.barathiraja.dinam.domain.repository.OccurrenceRepository

class OccurrenceRepositoryImpl(
    private val occurrenceDao: OccurrenceDao
) : OccurrenceRepository {

    override suspend fun getByPeriodDate(userId: String, periodDate: String): Occurrence? {
        return occurrenceDao.getByDate(userId, periodDate)?.let { OccurrenceMapper.toDomain(it) }
    }

    override suspend fun getOccurrencesFromDate(userId: String, fromDate: String): List<Occurrence> {
        return occurrenceDao.getFromDate(userId, fromDate).map { OccurrenceMapper.toDomain(it) }
    }

    override suspend fun getPastOccurrences(userId: String, todayDate: String): List<Occurrence> {
        return occurrenceDao.getPastOccurrences(userId, todayDate).map { OccurrenceMapper.toDomain(it) }
    }

    override suspend fun getById(id: String): Occurrence? {
        return occurrenceDao.getById(id)?.let { OccurrenceMapper.toDomain(it) }
    }

    override suspend fun insert(occurrence: Occurrence) {
        occurrenceDao.insert(OccurrenceMapper.toEntity(occurrence))
    }

    override suspend fun delete(occurrence: Occurrence) {
        occurrenceDao.delete(OccurrenceMapper.toEntity(occurrence))
    }
}