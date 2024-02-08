package org.hyperskill.app.paywall.injection

import org.hyperskill.app.paywall.domain.model.PaywallRepository

interface PaywallDataComponent {
    val paywallRepository: PaywallRepository
}