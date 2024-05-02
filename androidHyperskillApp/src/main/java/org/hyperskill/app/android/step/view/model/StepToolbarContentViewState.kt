package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature

sealed interface StepToolbarContentViewState {
    data class Practice(val stepQuizToolbarViewState: StepQuizToolbarFeature.ViewState) : StepToolbarContentViewState
    data class Theory(val title: String) : StepToolbarContentViewState
}