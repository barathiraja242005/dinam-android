package com.barathiraja.dinam.domain.usecase.occurrence

import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.model.OverdueItem
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class GetOccurrenceUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend fun getOccurrenceItems(
        occurrenceId: String,
        periodDate: String = ""
    ): List<OccurrenceItem> {
        return todayOccurrenceService.getOccurrenceItems(occurrenceId, periodDate)
    }

    suspend fun getOverdueItems(
        userId: String,
        todayDate: String
    ): List<OverdueItem> {
        return todayOccurrenceService.getOverdueItems(userId, todayDate)
    }
}