package org.hyperskill.step_quiz.presentation

import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizChildFeatureReducer
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer

internal fun StepQuizChildFeatureReducer.Companion.stub(stepRoute: StepRoute) =
    StepQuizChildFeatureReducer(
        stepQuizHintsReducer = StepQuizHintsReducer(stepRoute),
        stepQuizToolbarReducer = StepQuizToolbarReducer(stepRoute),
        stepQuizCodeBlanksReducer = StepQuizCodeBlanksReducer(stepRoute)
    )