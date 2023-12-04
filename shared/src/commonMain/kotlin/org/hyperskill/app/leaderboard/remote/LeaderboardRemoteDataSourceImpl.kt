package org.hyperskill.app.leaderboard.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.leaderboard.data.source.LeaderboardRemoteDataSource
import org.hyperskill.app.leaderboard.domain.model.Leaderboard

internal class LeaderboardRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : LeaderboardRemoteDataSource {
    override suspend fun getLeaderboard(): Result<Leaderboard> =
        kotlin.runCatching {
            httpClient
                .get("/api/leaderboard") {
                    contentType(ContentType.Application.Json)
                }.body()
        }
}