package org.hyperskill.app.progresses.injection

import org.hyperskill.app.progresses.data.flow.TopicProgressFlowImpl
import org.hyperskill.app.progresses.domain.flow.TopicProgressFlow

class ProgressesFlowDataComponentImpl : ProgressesFlowDataComponent {
    override val topicProgressFlow: TopicProgressFlow = TopicProgressFlowImpl()
}