package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.ManualViewModelFactory

actual interface PlatformMainComponent {
    val manualViewModelFactory: ManualViewModelFactory
}