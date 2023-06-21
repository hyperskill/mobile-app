package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformStepQuizComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}