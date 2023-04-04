package org.hyperskill.app.stage_implementation.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformStageImplementationComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}