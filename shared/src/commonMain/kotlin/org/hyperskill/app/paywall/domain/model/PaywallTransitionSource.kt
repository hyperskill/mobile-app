package org.hyperskill.app.paywall.domain.model

enum class PaywallTransitionSource(val analyticName: String) {
    LOGIN("login"),
    PROFILE_SETTINGS("profile_settings")
}