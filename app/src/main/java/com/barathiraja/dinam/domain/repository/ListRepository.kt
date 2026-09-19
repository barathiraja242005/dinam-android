package com.barathiraja.dinam.domain.repository

import com.barathiraja.dinam.domain.model.DinamList

interface ListRepository {
    suspend fun getActiveLists(userId: String): List<DinamList>
    suspend fun getListById(id: String): DinamList?
    suspend fun insertList(list: DinamList)
    suspend fun updateList(list: DinamList)
}