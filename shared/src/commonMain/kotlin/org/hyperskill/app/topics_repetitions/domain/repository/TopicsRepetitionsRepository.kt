package org.hyperskill.app.topics_repetitions.domain.repository

import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetition
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitionStatistics

interface TopicsRepetitionsRepository {
    suspend fun getTopicsRepetitions(pageSize: Int, page: Int): Result<List<TopicRepetition>>
    suspend fun getTopicsRepetitionStatistics(): Result<TopicsRepetitionStatistics>
}