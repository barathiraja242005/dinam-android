package com.barathiraja.dinam.data.repository

import com.barathiraja.dinam.data.local.dao.ListDao
import com.barathiraja.dinam.data.local.entity.ListEntity

class ListRepository(
    private val listDao: ListDao
) {

    suspend fun getActiveLists(
        userId: String
    ): List<ListEntity> {
        return listDao.getActiveLists(
            userId = userId
        )
    }

    suspend fun getList(
        listId: String
    ): ListEntity? {
        return listDao.getById(
            listId = listId
        )
    }

    suspend fun insertList(
        list: ListEntity
    ) {
        listDao.insert(list)
    }

    suspend fun updateList(
        list: ListEntity
    ) {
        listDao.update(list)
    }
}