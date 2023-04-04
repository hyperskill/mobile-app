package org.hyperskill.app.subscriptions.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.remote.model.SubscriptionsResponse

class SubscriptionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : SubscriptionsRemoteDataSource {
    override suspend fun getCurrentSubscription(): Result<Subscription> =
        kotlin.runCatching {
            httpClient.get("/api/subscriptions/current") {
                contentType(ContentType.Application.Json)
            }.body<SubscriptionsResponse>().subscriptions.first()
        }
}