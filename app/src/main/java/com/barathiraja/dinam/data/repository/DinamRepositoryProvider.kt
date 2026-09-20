package com.barathiraja.dinam.data.repository

import android.content.Context
import com.barathiraja.dinam.data.local.provider.DinamDatabaseProvider
import com.barathiraja.dinam.data.session.UserSession
import com.barathiraja.dinam.domain.repository.ItemStatRepository
import com.barathiraja.dinam.domain.repository.ListItemRepository
import com.barathiraja.dinam.domain.repository.ListRepository
import com.barathiraja.dinam.domain.repository.OccurrenceItemRepository
import com.barathiraja.dinam.domain.repository.OccurrenceRepository
import com.barathiraja.dinam.domain.repository.TodayRepository
import com.barathiraja.dinam.domain.repository.UserRepository
import com.barathiraja.dinam.domain.service.TodayOccurrenceService
import com.barathiraja.dinam.domain.usecase.list.AddListItemUseCase
import com.barathiraja.dinam.domain.usecase.list.CreateListUseCase
import com.barathiraja.dinam.domain.usecase.list.DeleteListItemUseCase
import com.barathiraja.dinam.domain.usecase.list.RenameListItemUseCase
import com.barathiraja.dinam.domain.usecase.list.SetListItemCheckedUseCase
import com.barathiraja.dinam.domain.usecase.occurrence.GenerateOccurrenceUseCase
import com.barathiraja.dinam.domain.usecase.occurrence.GetOccurrenceUseCase
import com.barathiraja.dinam.domain.usecase.occurrence.RescheduleOverdueItemUseCase
import com.barathiraja.dinam.domain.usecase.today.AddTodayItemUseCase
import com.barathiraja.dinam.domain.usecase.today.DeleteTodayItemUseCase
import com.barathiraja.dinam.domain.usecase.today.RenameTodayItemUseCase
import com.barathiraja.dinam.domain.usecase.today.SetTodayItemCheckedUseCase
import com.barathiraja.dinam.domain.usecase.today.SetTodayItemEverydayUseCase
import com.barathiraja.dinam.domain.usecase.today.UpdateTodayItemTimeUseCase

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
            listItemRepository = listItemRepository,
            listRepository = listRepository
        )

        val addTodayItemUseCase = AddTodayItemUseCase(todayOccurrenceService)
        val renameTodayItemUseCase = RenameTodayItemUseCase(todayOccurrenceService)
        val updateTodayItemTimeUseCase = UpdateTodayItemTimeUseCase(todayOccurrenceService)
        val setTodayItemCheckedUseCase = SetTodayItemCheckedUseCase(todayOccurrenceService)
        val deleteTodayItemUseCase = DeleteTodayItemUseCase(todayOccurrenceService)
        val setTodayItemEverydayUseCase = SetTodayItemEverydayUseCase(todayOccurrenceService)

        val getOccurrenceUseCase = GetOccurrenceUseCase(todayOccurrenceService)
        val generateOccurrenceUseCase = GenerateOccurrenceUseCase(todayOccurrenceService)
        val rescheduleOverdueItemUseCase = RescheduleOverdueItemUseCase(todayOccurrenceService)

        val createListUseCase = CreateListUseCase(listRepository)
        val addListItemUseCase = AddListItemUseCase(listItemRepository)
        val renameListItemUseCase = RenameListItemUseCase(listItemRepository)
        val deleteListItemUseCase = DeleteListItemUseCase(listItemRepository)
        val setListItemCheckedUseCase = SetListItemCheckedUseCase(listItemRepository)

        return Repositories(
            userRepository = userRepository,
            userSession = userSession,
            todayRepository = todayRepository,
            occurrenceRepository = occurrenceRepository,
            occurrenceItemRepository = occurrenceItemRepository,
            listRepository = listRepository,
            listItemRepository = listItemRepository,
            itemStatRepository = itemStatRepository,
            todayOccurrenceService = todayOccurrenceService,
            addTodayItemUseCase = addTodayItemUseCase,
            renameTodayItemUseCase = renameTodayItemUseCase,
            updateTodayItemTimeUseCase = updateTodayItemTimeUseCase,
            setTodayItemCheckedUseCase = setTodayItemCheckedUseCase,
            deleteTodayItemUseCase = deleteTodayItemUseCase,
            setTodayItemEverydayUseCase = setTodayItemEverydayUseCase,
            getOccurrenceUseCase = getOccurrenceUseCase,
            generateOccurrenceUseCase = generateOccurrenceUseCase,
            rescheduleOverdueItemUseCase = rescheduleOverdueItemUseCase,
            createListUseCase = createListUseCase,
            addListItemUseCase = addListItemUseCase,
            renameListItemUseCase = renameListItemUseCase,
            deleteListItemUseCase = deleteListItemUseCase,
            setListItemCheckedUseCase = setListItemCheckedUseCase
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
    val todayOccurrenceService: TodayOccurrenceService,

    val addTodayItemUseCase: AddTodayItemUseCase,
    val renameTodayItemUseCase: RenameTodayItemUseCase,
    val updateTodayItemTimeUseCase: UpdateTodayItemTimeUseCase,
    val setTodayItemCheckedUseCase: SetTodayItemCheckedUseCase,
    val deleteTodayItemUseCase: DeleteTodayItemUseCase,
    val setTodayItemEverydayUseCase: SetTodayItemEverydayUseCase,

    val getOccurrenceUseCase: GetOccurrenceUseCase,
    val generateOccurrenceUseCase: GenerateOccurrenceUseCase,
    val rescheduleOverdueItemUseCase: RescheduleOverdueItemUseCase,

    val createListUseCase: CreateListUseCase,
    val addListItemUseCase: AddListItemUseCase,
    val renameListItemUseCase: RenameListItemUseCase,
    val deleteListItemUseCase: DeleteListItemUseCase,
    val setListItemCheckedUseCase: SetListItemCheckedUseCase
)