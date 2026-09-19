package com.barathiraja.dinam.data.local.provider

import android.content.Context
import androidx.room.Room
import com.barathiraja.dinam.data.local.DinamDatabase

object DinamDatabaseProvider {

    @Volatile
    private var instance: DinamDatabase? = null

    fun getDatabase(context: Context): DinamDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                DinamDatabase::class.java,
                "dinam.db"
            ).fallbackToDestructiveMigration(true).build().also {
                instance = it
            }
        }
    }
}