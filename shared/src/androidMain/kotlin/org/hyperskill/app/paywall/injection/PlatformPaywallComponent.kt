package org.hyperskill.app.paywall.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory

interface PlatformPaywallComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
}