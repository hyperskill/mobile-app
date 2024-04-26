package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature

sealed interface StepToolbarViewState {
    data class Practice(val stepQuizToolbarViewState: StepQuizToolbarFeature.ViewState) : StepToolbarViewState
    data class Theory(val title: String) : StepToolbarViewState
}