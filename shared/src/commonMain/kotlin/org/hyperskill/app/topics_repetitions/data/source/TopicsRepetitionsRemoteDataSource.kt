package org.hyperskill.app.topics_repetitions.data.source

import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitionStatistics

interface TopicsRepetitionsRemoteDataSource {
    suspend fun getTopicsRepetitions(pageSize: Int, page: Int): Result<List<TopicRepetition>>
    suspend fun getTopicsRepetitionStatistics(): Result<TopicsRepetitionStatistics>
}