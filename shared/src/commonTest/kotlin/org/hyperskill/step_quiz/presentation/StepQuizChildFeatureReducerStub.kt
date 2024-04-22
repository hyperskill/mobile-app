package org.hyperskill.step_quiz.presentation

import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizChildFeatureReducer
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer

internal fun StepQuizChildFeatureReducer.Companion.stub(stepRoute: StepRoute) =
    StepQuizChildFeatureReducer(
        stepQuizHintsReducer = StepQuizHintsReducer(stepRoute),
        problemsLimitReducer = ProblemsLimitReducer(stepRoute)
    )