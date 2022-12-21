package org.hyperskill.app.items.domain.repository

import org.hyperskill.app.items.domain.model.Item

interface ItemsRepository {
    suspend fun getItems(): Result<List<Item>>
}