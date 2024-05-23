package org.hyperskill.app.topic_completed_modal.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.topics.domain.model.Topic

@Serializable
data class TopicCompletedModalFeatureParams(
    val topic: Topic,
    val passedTopicsCount: Int,
    val canContinueWithNextTopic: Boolean
)