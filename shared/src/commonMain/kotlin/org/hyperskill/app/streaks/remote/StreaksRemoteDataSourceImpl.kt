package org.hyperskill.app.streaks.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.streaks.data.source.StreaksRemoteDataSource
import org.hyperskill.app.streaks.remote.model.StreaksResponse

class StreaksRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : StreaksRemoteDataSource {
    override suspend fun getUserStreaks(userId: Long, page: Int): Result<StreaksResponse> =
        kotlin.runCatching {
            httpClient
                .get("/api/streaks") {
                    contentType(ContentType.Application.Json)
                    parameter("user", userId)
                    parameter("page", page)
                }.body()
        }
}