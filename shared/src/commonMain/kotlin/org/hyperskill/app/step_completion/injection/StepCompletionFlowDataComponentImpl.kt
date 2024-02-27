package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.step_completion.data.flow.DailyStepCompletedFlowImpl
import org.hyperskill.app.step_completion.data.flow.StepCompletedFlowImpl
import org.hyperskill.app.step_completion.data.flow.TopicCompletedFlowImpl
import org.hyperskill.app.step_completion.domain.flow.DailyStepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow

internal class StepCompletionFlowDataComponentImpl : StepCompletionFlowDataComponent {
    override val topicCompletedFlow: TopicCompletedFlow =
        TopicCompletedFlowImpl()

    override val dailyStepCompletedFlow: DailyStepCompletedFlow =
        DailyStepCompletedFlowImpl()

    override val stepCompletedFlow: StepCompletedFlow =
        StepCompletedFlowImpl()
}