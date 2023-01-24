package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.topics_repetitions.data.flow.TopicRepeatedFlowImpl
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow

class TopicsRepetitionsFlowDataComponentImpl : TopicsRepetitionsFlowDataComponent {
    override val topicRepeatedFlow: TopicRepeatedFlow = TopicRepeatedFlowImpl()
}