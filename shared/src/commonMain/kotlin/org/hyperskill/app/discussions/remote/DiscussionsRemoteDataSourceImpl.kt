package org.hyperskill.app.discussions.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.discussions.data.source.DiscussionsRemoteDataSource
import org.hyperskill.app.discussions.remote.model.DiscussionsRequest
import org.hyperskill.app.discussions.remote.model.DiscussionsResponse

class DiscussionsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : DiscussionsRemoteDataSource {
    override suspend fun getDiscussions(request: DiscussionsRequest): Result<DiscussionsResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/discussions") {
                    contentType(ContentType.Application.Json)
                    request.parameters.forEach { parameter(it.key, it.value.toString()) }
                }.body()
        }
}