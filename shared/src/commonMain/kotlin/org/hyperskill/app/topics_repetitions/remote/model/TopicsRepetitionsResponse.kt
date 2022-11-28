package org.hyperskill.app.topics_repetitions.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.core.remote.Meta
import org.hyperskill.app.core.remote.MetaResponse
import org.hyperskill.app.topics_repetitions.domain.model.TopicsRepetitions

@Serializable
class TopicsRepetitionsResponse(
    @SerialName("meta")
    override val meta: Meta,

    @SerialName("topics-repetitions")
    val topicsRepetitions: List<TopicsRepetitions>
) : MetaResponse