package com.barathiraja.dinam.data.local.mapper

import com.barathiraja.dinam.data.local.entity.UserEntity
import com.barathiraja.dinam.domain.model.User

object UserMapper {

    fun toDomain(entity: UserEntity): User {
        return User(id = entity.id)
    }

    fun toEntity(domain: User): UserEntity {
        return UserEntity(id = domain.id)
    }
}