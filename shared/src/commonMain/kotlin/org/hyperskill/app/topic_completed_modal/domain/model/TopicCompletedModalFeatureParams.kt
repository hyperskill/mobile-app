package org.hyperskill.app.topic_completed_modal.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.topics.domain.model.Topic

@Serializable
data class TopicCompletedModalFeatureParams(
    val topic: Topic,
    val passedTopicsCount: Int,
    val stepRoute: StepRoute,
    val continueBehaviour: ContinueBehaviour
) {
    enum class ContinueBehaviour {
        CONTINUE_WITH_NEXT_TOPIC,
        SHOW_PAYWALL,
        GO_TO_STUDY_PLAN
    }
}