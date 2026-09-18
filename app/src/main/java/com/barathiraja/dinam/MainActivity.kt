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
import com.barathiraja.dinam.ui.screens.home.HomeScreen
import com.barathiraja.dinam.ui.screens.today.TodayScreen
import com.barathiraja.dinam.ui.theme.DinamTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge().

        setContent {
            DinamTheme {
                DinamApp()
            }
        }
    }
}

@Composable
private fun DinamApp() {

    var showToday by remember {
        mutableStateOf(false)
    }

    if (showToday) {
        TodayScreen(
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