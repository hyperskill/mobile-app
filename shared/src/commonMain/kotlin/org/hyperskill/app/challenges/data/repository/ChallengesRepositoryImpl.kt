package org.hyperskill.app.challenges.data.repository

import org.hyperskill.app.challenges.data.source.ChallengesRemoteDataSource
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.domain.repository.ChallengesRepository

internal class ChallengesRepositoryImpl(
    private val challengesRemoteDataSource: ChallengesRemoteDataSource
) : ChallengesRepository {
    override suspend fun getChallenges(): Result<List<Challenge>> =
        challengesRemoteDataSource.getChallenges()
}