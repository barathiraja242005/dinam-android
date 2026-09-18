package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.UserDao
import com.barathiraja.dinam.data.local.entity.UserEntity

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun getUser(): UserEntity? {
        return userDao.getUser()
    }

    suspend fun createUser(
        user: UserEntity
    ) {
        userDao.insert(user)
    }
}