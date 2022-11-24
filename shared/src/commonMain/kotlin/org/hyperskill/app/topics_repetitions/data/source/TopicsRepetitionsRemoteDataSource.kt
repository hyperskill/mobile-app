package org.hyperskill.app.topics_repetitions.data.source

import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions

interface TopicsRepetitionsRemoteDataSource {
    suspend fun getTopicsRepetitions(isCurrentTrack: Boolean): Result<TopicsRepetitions>
}