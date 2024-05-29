package org.hyperskill.app.android.step.view.model

sealed interface StepMainMenuAction {
    object Theory : StepMainMenuAction
    data class OpenTheory(
        val isVisible: Boolean,
        val isEnabled: Boolean
    ) : StepMainMenuAction
}