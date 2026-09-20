package com.barathiraja.dinam.domain.usecase.today

import com.barathiraja.dinam.domain.model.OccurrenceItem
import com.barathiraja.dinam.domain.service.TodayOccurrenceService

class RenameTodayItemUseCase(
    private val todayOccurrenceService: TodayOccurrenceService
) {
    suspend operator fun invoke(
        userId: String,
        item: OccurrenceItem,
        newText: String,
        currentDate: String
    ) {
        todayOccurrenceService.renameItem(
            userId = userId,
            item = item,
            newText = newText,
            currentDate = currentDate
        )
    }
}