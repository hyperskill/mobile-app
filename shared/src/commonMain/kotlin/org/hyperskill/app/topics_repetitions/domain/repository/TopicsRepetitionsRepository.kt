package org.hyperskill.app.topics_repetitions.domain.repository

import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions

interface TopicsRepetitionsRepository {
    suspend fun getCurrentTrackTopicsRepetitions(): Result<TopicsRepetitions>
}