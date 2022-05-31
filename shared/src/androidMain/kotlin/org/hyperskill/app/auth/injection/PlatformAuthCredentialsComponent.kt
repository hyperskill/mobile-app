package org.hyperskill.app.auth.injection

import org.hyperskill.app.core.injection.ManualViewModelFactory

actual interface PlatformAuthCredentialsComponent {
    val manualViewModelFactory: ManualViewModelFactory
}