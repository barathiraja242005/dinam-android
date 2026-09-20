package com.barathiraja.dinam.domain.usecase.today

import com.barathiraja.dinam.domain.model.TodayItem
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class SetTodayItemEverydayUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend operator fun invoke(
        userId: String,
        item: TodayItem,
        enabled: Boolean,
        periodDate: String = item.activeFrom
    ) {
        todayOccurrenceService.setEveryDay(userId, item, enabled, periodDate)
    }
}