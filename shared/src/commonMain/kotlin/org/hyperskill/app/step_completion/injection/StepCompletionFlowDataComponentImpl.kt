package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.step_completion.data.flow.TopicCompletedFlowImpl
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow

class StepCompletionFlowDataComponentImpl : StepCompletionFlowDataComponent {
    override val topicCompletedFlow: TopicCompletedFlow = TopicCompletedFlowImpl()
}