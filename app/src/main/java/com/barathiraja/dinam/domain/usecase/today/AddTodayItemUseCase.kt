package com.barathiraja.dinam.domain.usecase.today

import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class AddTodayItemUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend operator fun invoke(
        userId: String,
        periodDate: String,
        item: TodayItem
    ) {
        todayOccurrenceService.addTodayItem(userId, periodDate, item)
    }
}