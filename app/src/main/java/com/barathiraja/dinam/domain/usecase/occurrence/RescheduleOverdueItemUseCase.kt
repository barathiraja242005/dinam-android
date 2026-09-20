package com.barathiraja.dinam.domain.usecase.occurrence

import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class RescheduleOverdueItemUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend operator fun invoke(
        userId: String,
        overdueItem: OccurrenceItem,
        targetDate: String,
        targetTime: String? = overdueItem.remindAt,
        remindMe: Boolean = false
    ) {
        todayOccurrenceService.rescheduleOverdueItem(
            userId = userId,
            overdueItem = overdueItem,
            targetDate = targetDate,
            targetTime = targetTime,
            remindMe = remindMe
        )
    }
}