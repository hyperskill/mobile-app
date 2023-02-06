package org.hyperskill.app.android.step.view.model

import org.hyperskill.app.step_completion.presentation.StepCompletionFeature

interface StepCompletionHost {
    fun onNewMessage(message: StepCompletionFeature.Message)
}