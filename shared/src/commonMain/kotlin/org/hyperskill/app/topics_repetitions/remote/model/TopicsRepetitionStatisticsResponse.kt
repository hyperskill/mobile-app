package org.hyperskill.app.topics_repetitions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.topics_repetitions.domain.model.TopicRepetitionStatistics

@Serializable
class TopicsRepetitionStatisticsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("topics-repetition")
    val topicsRepetitionStatistics: List<TopicRepetitionStatistics>
) : MetaResponse