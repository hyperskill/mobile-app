package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformStepComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}