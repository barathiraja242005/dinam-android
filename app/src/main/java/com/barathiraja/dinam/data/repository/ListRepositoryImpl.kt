package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ListDao
import com.barathiraja.dinam.data.local.entity.ListEntity
import com.barathiraja.dinam.domain.model.DinamList
import com.barathiraja.dinam.domain.repository.ListRepository

class ListRepositoryImpl(
    private val listDao: ListDao
) : ListRepository {

    override suspend fun getActiveLists(userId: String): List<DinamList> {
        return listDao.getActiveLists(userId).map { it.toDomain() }
    }

    override suspend fun getListById(id: String): DinamList? {
        return listDao.getById(id)?.toDomain()
    }

    override suspend fun insertList(list: DinamList) {
        listDao.insert(list.toEntity())
    }

    override suspend fun updateList(list: DinamList) {
        listDao.update(list.toEntity())
    }

    private fun ListEntity.toDomain(): DinamList {
        return DinamList(
            id = id,
            userId = userId,
            title = title,
            category = category,
            archivedAt = archivedAt,
            createdAt = createdAt
        )
    }

    private fun DinamList.toEntity(): ListEntity {
        return ListEntity(
            id = id,
            userId = userId,
            title = title,
            category = category,
            archivedAt = archivedAt,
            createdAt = createdAt
        )
    }
}