package org.hyperskill.app.challenges.data.source

import org.hyperskill.app.challenges.domain.model.Challenge

interface ChallengesRemoteDataSource {
    suspend fun getChallenges(): Result<List<Challenge>>
}