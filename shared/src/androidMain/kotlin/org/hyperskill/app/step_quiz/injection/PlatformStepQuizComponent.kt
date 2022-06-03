package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformStepQuizComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}