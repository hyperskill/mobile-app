package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.data.flow.StepSolvedFlowImpl
import org.hyperskill.app.step_quiz.domain.flow.StepSolvedFlow

internal class StepsFlowDataComponentImpl : StepsFlowDataComponent {
    override val stepSolvedFlow: StepSolvedFlow =
        StepSolvedFlowImpl()
}