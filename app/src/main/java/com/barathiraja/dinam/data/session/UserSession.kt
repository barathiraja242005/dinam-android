package com.barathiraja.dinam.data.session

import com.barathiraja.dinam.domain.model.User
import com.barathiraja.dinam.domain.repository.UserRepository
import com.barathiraja.dinam.domain.util.IdGenerator

class UserSession(
    private val userRepository: UserRepository,
    private val idGenerator: IdGenerator = IdGenerator.Default
) {

    suspend fun getCurrentUser(): User {
        return userRepository.getCurrentUser()
            ?: createUser()
    }

    private suspend fun createUser(): User {
        val user = User(
            id = idGenerator.generateId()
        )

        userRepository.insertUser(user)

        return user
    }
}