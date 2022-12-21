package org.hyperskill.app.items.domain.interactor

import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.items.domain.repository.ItemsRepository

class ItemsInteractor(
    private val itemsRepository: ItemsRepository
) {
    suspend fun getItems(): Result<List<Item>> =
        itemsRepository.getItems()
}