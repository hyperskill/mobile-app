package org.hyperskill.app.paywall.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.paywall.presentation.PaywallViewModel

class PlatformPaywallComponentImpl(
    private val paywallComponent: PaywallComponent
) : PlatformPaywallComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                PaywallViewModel::class.java to {
                    PaywallViewModel(
                        paywallComponent.paywallFeature.wrapWithFlowView()
                    )
                }
            )
        )
}