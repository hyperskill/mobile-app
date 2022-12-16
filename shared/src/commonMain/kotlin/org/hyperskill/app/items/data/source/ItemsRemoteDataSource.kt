package org.hyperskill.app.items.data.source

import org.hyperskill.app.items.domain.model.Item

interface ItemsRemoteDataSource {
    suspend fun getItems(): Result<List<Item>>
}