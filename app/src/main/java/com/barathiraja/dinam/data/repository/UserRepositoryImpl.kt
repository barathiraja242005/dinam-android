package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.UserDao
import com.barathiraja.dinam.data.local.entity.UserEntity
import com.barathiraja.dinam.domain.model.User
import com.barathiraja.dinam.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getCurrentUser(): User? {
        return userDao.getUser()?.toDomain()
    }

    override suspend fun insertUser(user: User) {
        userDao.insert(user.toEntity())
    }

    private fun UserEntity.toDomain(): User {
        return User(id = id)
    }

    private fun User.toEntity(): UserEntity {
        return UserEntity(id = id)
    }
}