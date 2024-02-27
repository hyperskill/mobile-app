package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.step_quiz.domain.flow.StepSolvedFlow

interface StepsFlowDataComponent {
    val stepSolvedFlow: StepSolvedFlow
}