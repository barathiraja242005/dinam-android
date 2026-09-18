package com.barathiraja.dinam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.barathiraja.dinam.data.repository.Repositories
import com.barathiraja.dinam.ui.screens.home.HomeScreen
import com.barathiraja.dinam.ui.screens.today.TodayScreen
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
                todayOccurrenceService = repositories.todayOccurrenceService,
                userRepository = repositories.userRepository
            )
        )[TodayViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DinamTheme {
                DinamApp(
                    todayViewModel = todayViewModel
                )
            }
        }
    }
}

@Composable
private fun DinamApp(
    todayViewModel: TodayViewModel
) {

    var showToday by remember {
        mutableStateOf(false)
    }

    if (showToday) {
        TodayScreen(
            todayViewModel = todayViewModel,
            onBack = {
                showToday = false
            }
        )
    } else {
        HomeScreen(
            onOpenToday = {
                showToday = true
            }
        )
    }
}