package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.Occurrence

interface OccurrenceRepository {
    suspend fun getByPeriodDate(userId: String, periodDate: String): Occurrence?
    suspend fun getOccurrencesFromDate(userId: String, fromDate: String): List<Occurrence>
    suspend fun getPastOccurrences(userId: String, todayDate: String): List<Occurrence>
    suspend fun getById(id: String): Occurrence?
    suspend fun insert(occurrence: Occurrence)
    suspend fun delete(occurrence: Occurrence)
}