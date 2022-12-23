package org.hyperskill.app.items.data.repository

import org.hyperskill.app.items.data.source.ItemsRemoteDataSource
import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.items.domain.repository.ItemsRepository

class ItemsRepositoryImpl(
    private val itemsRemoteDataSource: ItemsRemoteDataSource
) : ItemsRepository {
    override suspend fun getItems(): Result<List<Item>> =
        itemsRemoteDataSource.getItems()
}