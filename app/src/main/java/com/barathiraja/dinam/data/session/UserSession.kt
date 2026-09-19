package com.barathiraja.dinam.data.session

import com.barathiraja.dinam.data.local.entity.UserEntity
import com.barathiraja.dinam.data.repository.UserRepository
import java.util.UUID

class UserSession(
    private val userRepository: UserRepository
) {

    suspend fun getCurrentUser(): UserEntity {
        return userRepository.getUser()
            ?: createUser()
    }

    private suspend fun createUser(): UserEntity {
        val user = UserEntity(
            id = UUID.randomUUID().toString()
        )

        userRepository.createUser(user)

        return user
    }
}