package org.hyperskill.app.step_feedback.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformStepFeedbackComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}