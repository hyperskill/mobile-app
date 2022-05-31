package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.ManualViewModelFactory

actual interface PlatformStepComponent {
    val manualViewModelFactory: ManualViewModelFactory
}