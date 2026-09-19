package com.barathiraja.dinam.data.repository

import android.content.Context
import com.barathiraja.dinam.data.local.provider.DinamDatabaseProvider
import com.barathiraja.dinam.data.service.TodayOccurrenceService
import com.barathiraja.dinam.data.session.UserSession

object DinamRepositoryProvider {

    fun create(context: Context): Repositories {
        val database = DinamDatabaseProvider.getDatabase(context)

        val userRepository = UserRepository(
            userDao = database.userDao()
        )

        val userSession = UserSession(
            userRepository = userRepository
        )

        val todayRepository = TodayRepository(
            todayItemDao = database.todayItemDao()
        )

        val occurrenceRepository = OccurrenceRepository(
            occurrenceDao = database.occurrenceDao()
        )

        val occurrenceItemRepository = OccurrenceItemRepository(
            occurrenceItemDao = database.occurrenceItemDao()
        )

        val listRepository = ListRepository(
            listDao = database.listDao()
        )

        val listItemRepository = ListItemRepository(
            listItemDao = database.listItemDao()
        )

        val itemStatRepository = ItemStatRepository(
            itemStatDao = database.itemStatDao()
        )

        val todayOccurrenceService = TodayOccurrenceService(
            occurrenceRepository = occurrenceRepository,
            occurrenceItemRepository = occurrenceItemRepository,
            todayRepository = todayRepository
        )

        return Repositories(
            userRepository = userRepository,
            userSession = userSession,
            todayRepository = todayRepository,
            occurrenceRepository = occurrenceRepository,
            occurrenceItemRepository = occurrenceItemRepository,
            listRepository = listRepository,
            listItemRepository = listItemRepository,
            itemStatRepository = itemStatRepository,
            todayOccurrenceService = todayOccurrenceService
        )
    }
}

data class Repositories(
    val userRepository: UserRepository,
    val userSession: UserSession,
    val todayRepository: TodayRepository,
    val occurrenceRepository: OccurrenceRepository,
    val occurrenceItemRepository: OccurrenceItemRepository,
    val listRepository: ListRepository,
    val listItemRepository: ListItemRepository,
    val itemStatRepository: ItemStatRepository,
    val todayOccurrenceService: TodayOccurrenceService
)