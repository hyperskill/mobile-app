package org.hyperskill.app.purchases.domain.model

internal expect object PlatformProductIdentifiers {
    val MOBILE_ONLY_MONTHLY_SUBSCRIPTION: String
    val MOBILE_ONLY_YEARLY_SUBSCRIPTION: String
}