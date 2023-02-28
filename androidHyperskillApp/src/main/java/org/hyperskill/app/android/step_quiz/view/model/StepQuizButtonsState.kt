package org.hyperskill.app.android.step_quiz.view.model

sealed interface StepQuizButtonsState {
    object Submit : StepQuizButtonsState
    object Retry : StepQuizButtonsState
    object Continue : StepQuizButtonsState
    object RetryLogoAndSubmit : StepQuizButtonsState
    object RetryLogoAndContinue : StepQuizButtonsState
}