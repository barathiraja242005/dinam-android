package com.barathiraja.dinam.domain.usecase.today

import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class UpdateTodayItemTimeUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend operator fun invoke(
        item: OccurrenceItem,
        newTime: String?
    ) {
        todayOccurrenceService.updateItemTime(item, newTime)
    }
}