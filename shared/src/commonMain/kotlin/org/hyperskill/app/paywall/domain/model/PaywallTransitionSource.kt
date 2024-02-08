package org.hyperskill.app.paywall.domain.model

enum class PaywallTransitionSource(val analyticName: String) {
    APP_STARTUP("app_startup"),
    LOGIN("login"),
    PROFILE_SETTINGS("profile_settings")
}