package org.hyperskill.app.items.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.hyperskill.app.items.data.source.ItemsRemoteDataSource
import org.hyperskill.app.items.domain.model.Item
import org.hyperskill.app.items.remote.model.ItemsResponse

class ItemsRemoteDataSourceImpl(private val httpClient: HttpClient) : ItemsRemoteDataSource {
    override suspend fun getItems(): Result<List<Item>> =
        kotlin.runCatching {
            httpClient.get("/api/items").body<ItemsResponse>().items
        }
}