package org.hyperskill.app.topics_repetitions.data.source

import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics

interface TopicsRepetitionsRemoteDataSource {
    suspend fun getTopicsRepetitions(pageSize: Int, page: Int, isInCurrentTrack: Boolean): Result<List<TopicRepetition>>
    suspend fun getTopicsRepetitionStatistics(isInCurrentTrack: Boolean): Result<TopicRepetitionStatistics>
}