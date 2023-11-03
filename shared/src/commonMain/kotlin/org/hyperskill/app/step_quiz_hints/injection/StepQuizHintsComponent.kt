package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsActionDispatcher
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer

interface StepQuizHintsComponent {
    val stepQuizHintsReducer: StepQuizHintsReducer
    val stepQuizHintsActionDispatcher: StepQuizHintsActionDispatcher
}