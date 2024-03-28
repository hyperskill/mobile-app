package org.hyperskill.app.step_theory_feedback.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformStepTheoryFeedbackComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}