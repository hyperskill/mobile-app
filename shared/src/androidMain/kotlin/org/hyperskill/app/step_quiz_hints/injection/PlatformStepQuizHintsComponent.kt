package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformStepQuizHintsComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}