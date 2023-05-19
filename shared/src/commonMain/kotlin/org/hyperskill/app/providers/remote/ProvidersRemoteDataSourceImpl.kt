package org.hyperskill.app.providers.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.network.remote.parameterIds
import org.hyperskill.app.providers.data.source.ProvidersRemoteDataSource
import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.providers.remote.model.ProvidersResponse

class ProvidersRemoteDataSourceImpl(
    private val httpclient: HttpClient
) : ProvidersRemoteDataSource {
    override suspend fun getProviders(providersIds: List<Long>): Result<List<Provider>> =
        kotlin.runCatching {
            if (providersIds.isEmpty()) {
                return Result.success(emptyList())
            }

            httpclient
                .get("/api/providers") {
                    contentType(ContentType.Application.Json)
                    parameterIds(providersIds)
                }.body<ProvidersResponse>().providers
        }
}