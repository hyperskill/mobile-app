package org.hyperskill.app.step_quiz_toolbar.presentation

import org.hyperskill.app.step.domain.model.StepRoute

object StepQuizToolbarResolver {
    fun isToolbarAvailable(stepRoute: StepRoute): Boolean =
        when (stepRoute) {
            is StepRoute.Learn.Step ->
                true
            is StepRoute.Learn.TheoryOpenedFromPractice,
            is StepRoute.Learn.TheoryOpenedFromSearch,
            is StepRoute.LearnDaily,
            is StepRoute.Repeat.Practice,
            is StepRoute.Repeat.Theory,
            is StepRoute.StageImplement ->
                false
        }
}