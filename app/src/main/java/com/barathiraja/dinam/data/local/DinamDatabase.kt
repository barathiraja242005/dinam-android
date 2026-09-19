package com.barathiraja.dinam.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.barathiraja.dinam.data.local.dao.ItemStatDao
import com.barathiraja.dinam.data.local.dao.ListDao
import com.barathiraja.dinam.data.local.dao.ListItemDao
import com.barathiraja.dinam.data.local.dao.OccurrenceDao
import com.barathiraja.dinam.data.local.dao.OccurrenceItemDao
import com.barathiraja.dinam.data.local.dao.TodayItemDao
import com.barathiraja.dinam.data.local.dao.UserDao
import com.barathiraja.dinam.data.local.entity.ItemStatEntity
import com.barathiraja.dinam.data.local.entity.ListEntity
import com.barathiraja.dinam.data.local.entity.ListItemEntity
import com.barathiraja.dinam.data.local.entity.OccurrenceEntity
import com.barathiraja.dinam.data.local.entity.OccurrenceItemEntity
import com.barathiraja.dinam.data.local.entity.TodayItemEntity
import com.barathiraja.dinam.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TodayItemEntity::class,
        OccurrenceEntity::class,
        OccurrenceItemEntity::class,
        ListEntity::class,
        ListItemEntity::class,
        ItemStatEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class DinamDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun todayItemDao(): TodayItemDao

    abstract fun occurrenceDao(): OccurrenceDao

    abstract fun occurrenceItemDao(): OccurrenceItemDao

    abstract fun listDao(): ListDao

    abstract fun listItemDao(): ListItemDao

    abstract fun itemStatDao(): ItemStatDao
}