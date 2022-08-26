package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticTarget(val targetName: String) {
    SEND("send"),
    ALLOW("allow"),
    DENY("deny"),
    DONE("done"),
    THEME("theme"),
    JETBRAINS_TERMS_OF_SERVICE("jetbrains_terms_of_service"),
    HYPERSKILL_TERMS_OF_SERVICE("hyperskill_terms_of_service"),
    HELP_CENTER("help_center"),
    LOGOUT("logout"),
    DELETE_ACCOUNT("delete_account"),
    HOME("home"),
    TRACK("track"),
    PROFILE("profile"),
    NOTIFICATIONS_SYSTEM_NOTICE("notifications_system_notice")
}