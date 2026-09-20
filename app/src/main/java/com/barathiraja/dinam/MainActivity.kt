@file:Suppress("NewApi")

package com.barathiraja.dinam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.barathiraja.dinam.app.navigation.AppNavigation
import com.barathiraja.dinam.data.repository.Repositories
import com.barathiraja.dinam.ui.screens.home.HomeViewModel
import com.barathiraja.dinam.ui.screens.home.HomeViewModelFactory
import com.barathiraja.dinam.ui.screens.list.ListDetailViewModel
import com.barathiraja.dinam.ui.screens.list.ListDetailViewModelFactory
import com.barathiraja.dinam.ui.screens.today.TodayViewModel
import com.barathiraja.dinam.ui.screens.today.TodayViewModelFactory
import com.barathiraja.dinam.ui.theme.DinamTheme

class MainActivity : ComponentActivity() {

    private val repositories: Repositories
        get() = (application as DinamApplication).repositories

    private val todayViewModel: TodayViewModel by lazy {
        ViewModelProvider(
            this,
            TodayViewModelFactory(
                getOccurrenceUseCase = repositories.getOccurrenceUseCase,
                addTodayItemUseCase = repositories.addTodayItemUseCase,
                renameTodayItemUseCase = repositories.renameTodayItemUseCase,
                updateTodayItemTimeUseCase = repositories.updateTodayItemTimeUseCase,
                setTodayItemCheckedUseCase = repositories.setTodayItemCheckedUseCase,
                deleteTodayItemUseCase = repositories.deleteTodayItemUseCase,
                setTodayItemEverydayUseCase = repositories.setTodayItemEverydayUseCase,
                rescheduleOverdueItemUseCase = repositories.rescheduleOverdueItemUseCase,
                userSession = repositories.userSession
            )
        )[TodayViewModel::class.java]
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            HomeViewModelFactory(
                listRepository = repositories.listRepository,
                userSession = repositories.userSession
            )
        )[HomeViewModel::class.java]
    }

    private val listDetailViewModel: ListDetailViewModel by lazy {
        ViewModelProvider(
            this,
            ListDetailViewModelFactory(
                listItemRepository = repositories.listItemRepository,
                addListItemUseCase = repositories.addListItemUseCase,
                renameListItemUseCase = repositories.renameListItemUseCase,
                deleteListItemUseCase = repositories.deleteListItemUseCase,
                setListItemCheckedUseCase = repositories.setListItemCheckedUseCase
            )
        )[ListDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DinamTheme {
                AppNavigation(
                    todayViewModel = todayViewModel,
                    homeViewModel = homeViewModel,
                    listDetailViewModel = listDetailViewModel,
                    repositories = repositories
                )
            }
        }
    }
}