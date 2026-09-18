package com.barathiraja.dinam.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.barathiraja.dinam.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query(
        """
        SELECT * FROM user
        LIMIT 1
        """
    )
    suspend fun getUser(): UserEntity?

    @Insert
    suspend fun insert(
        user: UserEntity
    )
}