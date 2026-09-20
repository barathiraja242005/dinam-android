package com.barathiraja.dinam.domain.usecase.today

import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class DeleteTodayItemUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend fun removeJustToday(
        userId: String,
        todayItemId: String,
        periodDate: String
    ) {
        todayOccurrenceService.removeJustToday(userId, todayItemId, periodDate)
    }

    suspend fun removeTodayAndFuture(
        userId: String,
        todayItemId: String,
        periodDate: String
    ) {
        todayOccurrenceService.removeTodayAndFuture(userId, todayItemId, periodDate)
    }
}