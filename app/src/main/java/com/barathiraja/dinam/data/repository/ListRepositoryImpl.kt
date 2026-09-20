package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ListDao
import com.barathiraja.dinam.data.local.mapper.ListMapper
import com.barathiraja.dinam.domain.model.DinamList
import com.barathiraja.dinam.domain.repository.ListRepository

class ListRepositoryImpl(
    private val listDao: ListDao
) : ListRepository {

    override suspend fun getActiveLists(userId: String): List<DinamList> {
        return listDao.getActiveLists(userId).map { ListMapper.toDomain(it) }
    }

    override suspend fun getListById(id: String): DinamList? {
        return listDao.getById(id)?.let { ListMapper.toDomain(it) }
    }

    override suspend fun insertList(list: DinamList) {
        listDao.insert(ListMapper.toEntity(list))
    }

    override suspend fun updateList(list: DinamList) {
        listDao.update(ListMapper.toEntity(list))
    }
}