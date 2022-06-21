package org.hyperskill.app.streak.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.hyperskill.app.streak.data.source.StreakRemoteDataSource
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.remote.model.StreakResponse

class StreakRemoteDataSoruceImpl(
    private val httpClient: HttpClient
) : StreakRemoteDataSource {
    override suspend fun getStreaks(userId: Long): Result<List<Streak>> =
        kotlin.runCatching {
            var page = 1
            val streakResponses = mutableListOf<List<Streak>>()
            do {
                val response = httpClient
                    .get("/api/streaks") {
                        contentType(ContentType.Application.Json)
                        parameter("user", userId)
                        parameter("page", page)
                    }.body<StreakResponse>()
                streakResponses.add(response.streaks)
                page++
            } while (response.meta.hasNext)

            return Result.success(streakResponses.flatten())
        }
}