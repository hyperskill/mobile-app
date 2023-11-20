package org.hyperskill.app.challenges.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.hyperskill.app.challenges.data.source.ChallengesRemoteDataSource
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.remote.model.ChallengesResponse

internal class ChallengesRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ChallengesRemoteDataSource {
    override suspend fun getChallenges(): Result<List<Challenge>> =
        kotlin.runCatching {
            httpClient
                .get("/api/challenges")
                .body<ChallengesResponse>()
                .challenges
        }
}