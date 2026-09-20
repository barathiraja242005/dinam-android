package com.barathiraja.dinam.domain.usecase.list

import com.barathiraja.dinam.domain.model.DinamList
import com.barathiraja.dinam.domain.repository.ListRepository

class CreateListUseCase(
    private val listRepository: ListRepository
) {
    suspend operator fun invoke(list: DinamList) {
        listRepository.insertList(list)
    }
}