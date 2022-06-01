package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

actual interface PlatformStepComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}