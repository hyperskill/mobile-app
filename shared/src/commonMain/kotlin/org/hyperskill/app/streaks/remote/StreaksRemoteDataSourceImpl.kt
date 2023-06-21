package org.hyperskill.app.streaks.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    override suspend fun recoverStreak(): Result<StreaksResponse> =
        kotlin.runCatching {
            httpClient
                .post("/api/streaks/recover")
                .body()
        }

    override suspend fun cancelStreakRecovery(): Result<StreaksResponse> =
        kotlin.runCatching {
            httpClient
                .post("/api/streaks/cancel-recovery")
                .body()
        }
}