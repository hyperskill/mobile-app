package org.hyperskill.app.progress_screen.injection

import org.hyperskill.app.progress_screen.data.flow.TopicProgressFlowImpl
import org.hyperskill.app.progress_screen.domain.flow.TopicProgressFlow

class ProgressesFlowDataComponentImpl : ProgressesFlowDataComponent {
    override val topicProgressFlow: TopicProgressFlow = TopicProgressFlowImpl()
}