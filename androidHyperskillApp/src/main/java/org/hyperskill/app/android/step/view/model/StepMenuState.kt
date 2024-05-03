package org.hyperskill.app.android.step.view.model

sealed interface StepMenuState {
    object TheoryFeedback : StepMenuState
    data class OpenTheory(
        val isVisible: Boolean,
        val isEnabled: Boolean
    ) : StepMenuState
}