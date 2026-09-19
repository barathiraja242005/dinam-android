package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.User

interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun insertUser(user: User)
}