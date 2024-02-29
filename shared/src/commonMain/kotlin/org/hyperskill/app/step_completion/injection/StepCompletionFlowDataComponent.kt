package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.step_completion.domain.flow.DailyStepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow

interface StepCompletionFlowDataComponent {
    val topicCompletedFlow: TopicCompletedFlow
    val dailyStepCompletedFlow: DailyStepCompletedFlow
    val stepCompletedFlow: StepCompletedFlow
}