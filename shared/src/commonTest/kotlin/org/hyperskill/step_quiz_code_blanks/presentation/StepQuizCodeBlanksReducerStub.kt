package org.hyperskill.step_quiz_code_blanks.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksOnboardingReducer
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

internal fun StepQuizCodeBlanksReducer.Companion.stub(
    stepRoute: StepRoute = StepRoute.Learn.Step(1, null)
): StepQuizCodeBlanksReducer =
    StepQuizCodeBlanksReducer(stepRoute, StepQuizCodeBlanksOnboardingReducer())