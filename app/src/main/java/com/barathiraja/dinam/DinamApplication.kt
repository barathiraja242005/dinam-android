package com.barathiraja.dinam

import android.app.Application
import com.barathiraja.dinam.data.repository.DinamRepositoryProvider
import com.barathiraja.dinam.data.repository.Repositories

class DinamApplication : Application() {

    val repositories: Repositories by lazy {
        DinamRepositoryProvider.create(this)
    }
}