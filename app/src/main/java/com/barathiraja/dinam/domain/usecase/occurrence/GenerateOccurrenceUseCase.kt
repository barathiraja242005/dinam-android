package com.barathiraja.dinam.domain.usecase.occurrence

import com.barathiraja.dinam.domain.model.Occurrence
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class GenerateOccurrenceUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend operator fun invoke(
        userId: String,
        periodDate: String
    ): Occurrence {
        return todayOccurrenceService.getOrCreateOccurrence(userId, periodDate)
    }
}