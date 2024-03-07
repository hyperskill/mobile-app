package org.hyperskill.app.paywall.view

import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

object PaywallToolbarVisibilityResolver {
    fun isToolbarVisible(paywallTransitionSource: PaywallTransitionSource): Boolean =
        when (paywallTransitionSource) {
            PaywallTransitionSource.APP_BECOMES_ACTIVE,
            PaywallTransitionSource.LOGIN -> false
            PaywallTransitionSource.MANAGE_SUBSCRIPTION,
            PaywallTransitionSource.PROFILE_SETTINGS,
            PaywallTransitionSource.PROBLEMS_LIMIT_MODAL -> true
        }
}