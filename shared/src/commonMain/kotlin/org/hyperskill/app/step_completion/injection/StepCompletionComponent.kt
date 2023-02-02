package org.hyperskill.app.step_completion.injection

import org.hyperskill.app.step_completion.presentation.StepCompletionActionDispatcher
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer

interface StepCompletionComponent {
    val stepCompletionReducer: StepCompletionReducer
    val stepCompletionActionDispatcher: StepCompletionActionDispatcher
}