package org.hyperskill.app.paywall.domain.model

enum class PaywallTransitionSource(val analyticName: String) {
    APP_BECOMES_ACTIVE("app_becomes_active"),
    PROFILE_SETTINGS("profile_settings"),
    PROBLEMS_LIMIT_MODAL("problems_limit_modal"),
    TOPIC_COMPLETED_MODAL("topic_completed_modal"),
    STUDY_PLAN("study_plan"),
    MANAGE_SUBSCRIPTION("manage_subscription")
}