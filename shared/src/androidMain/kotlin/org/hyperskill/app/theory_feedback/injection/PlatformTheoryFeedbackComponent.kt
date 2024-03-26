package org.hyperskill.app.theory_feedback.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformTheoryFeedbackComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}