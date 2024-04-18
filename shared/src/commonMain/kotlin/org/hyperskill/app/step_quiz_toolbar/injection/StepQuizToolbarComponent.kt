package org.hyperskill.app.step_quiz_toolbar.injection

import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarActionDispatcher
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer

interface StepQuizToolbarComponent {
    val stepQuizToolbarReducer: StepQuizToolbarReducer
    val stepQuizToolbarActionDispatcher: StepQuizToolbarActionDispatcher
}