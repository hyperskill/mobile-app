package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow

interface TopicsRepetitionsFlowDataComponent {
    val topicRepeatedFlow: TopicRepeatedFlow
}