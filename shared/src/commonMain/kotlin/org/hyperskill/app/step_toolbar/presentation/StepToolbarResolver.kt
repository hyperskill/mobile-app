package org.hyperskill.app.step_toolbar.presentation

import org.hyperskill.app.step.domain.model.StepRoute

object StepToolbarResolver {
    fun isProgressBarAvailable(stepRoute: StepRoute): Boolean =
        when (stepRoute) {
            is StepRoute.Learn.Step,
            is StepRoute.Learn.TheoryOpenedFromPractice ->
                true
            is StepRoute.LearnDaily,
            is StepRoute.Repeat.Practice,
            is StepRoute.Repeat.Theory,
            is StepRoute.StageImplement,
            is StepRoute.Learn.TheoryOpenedFromSearch ->
                false
        }
}