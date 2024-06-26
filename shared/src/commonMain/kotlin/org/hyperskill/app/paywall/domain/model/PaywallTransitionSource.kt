package org.hyperskill.app.paywall.domain.model

enum class PaywallTransitionSource(val analyticName: String) {
    APP_BECOMES_ACTIVE("app_becomes_active"),
    PROFILE_SETTINGS("profile_settings"),
    PROBLEMS_LIMIT_MODAL("problems_limit_modal"),
    MANAGE_SUBSCRIPTION("manage_subscription")
}