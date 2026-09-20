package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.UserDao
import com.barathiraja.dinam.data.local.mapper.UserMapper
import com.barathiraja.dinam.domain.model.User
import com.barathiraja.dinam.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getCurrentUser(): User? {
        return userDao.getUser()?.let { UserMapper.toDomain(it) }
    }

    override suspend fun insertUser(user: User) {
        userDao.insert(UserMapper.toEntity(user))
    }
}