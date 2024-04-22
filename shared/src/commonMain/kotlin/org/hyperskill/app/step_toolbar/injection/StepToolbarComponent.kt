package org.hyperskill.app.step_toolbar.injection

import org.hyperskill.app.step_toolbar.presentation.StepToolbarActionDispatcher
import org.hyperskill.app.step_toolbar.presentation.StepToolbarReducer

interface StepToolbarComponent {
    val stepToolbarReducer: StepToolbarReducer
    val stepToolbarActionDispatcher: StepToolbarActionDispatcher
}