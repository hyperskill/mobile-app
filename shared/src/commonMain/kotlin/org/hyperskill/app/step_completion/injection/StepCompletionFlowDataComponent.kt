package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow

interface StepCompletionFlowDataComponent {
    val topicCompletedFlow: TopicCompletedFlow
}