package com.barathiraja.dinam.data.repository

import android.content.Context
import com.barathiraja.dinam.data.local.provider.DinamDatabaseProvider
import com.barathiraja.dinam.data.service.TodayOccurrenceService
import com.barathiraja.dinam.data.session.UserSession
import com.barathiraja.dinam.domain.repository.ItemStatRepository
import com.barathiraja.dinam.domain.repository.ListItemRepository
import com.barathiraja.dinam.domain.repository.ListRepository
import com.barathiraja.dinam.domain.repository.OccurrenceItemRepository
import com.barathiraja.dinam.domain.repository.OccurrenceRepository
import com.barathiraja.dinam.domain.repository.TodayRepository
import com.barathiraja.dinam.domain.repository.UserRepository

object DinamRepositoryProvider {

    fun create(context: Context): Repositories {
        val database = DinamDatabaseProvider.getDatabase(context)

        val userRepository: UserRepository = UserRepositoryImpl(
            userDao = database.userDao()
        )

        val userSession = UserSession(
            userRepository = userRepository
        )

        val todayRepository: TodayRepository = TodayRepositoryImpl(
            todayItemDao = database.todayItemDao()
        )

        val occurrenceRepository: OccurrenceRepository = OccurrenceRepositoryImpl(
            occurrenceDao = database.occurrenceDao()
        )

        val occurrenceItemRepository: OccurrenceItemRepository = OccurrenceItemRepositoryImpl(
            occurrenceItemDao = database.occurrenceItemDao()
        )

        val listRepository: ListRepository = ListRepositoryImpl(
            listDao = database.listDao()
        )

        val listItemRepository: ListItemRepository = ListItemRepositoryImpl(
            listItemDao = database.listItemDao()
        )

        val itemStatRepository: ItemStatRepository = ItemStatRepositoryImpl(
            itemStatDao = database.itemStatDao()
        )

        val todayOccurrenceService = TodayOccurrenceService(
            occurrenceRepository = occurrenceRepository,
            occurrenceItemRepository = occurrenceItemRepository,
            todayRepository = todayRepository,
            listItemRepository = listItemRepository
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