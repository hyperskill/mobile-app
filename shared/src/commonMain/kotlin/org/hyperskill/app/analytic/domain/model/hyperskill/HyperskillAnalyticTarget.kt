package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticTarget(val targetName: String) {
    HOME("home"),
    TRACK("track"),
    PROFILE("profile"),
    SEND("send"),
    ALLOW("allow"),
    DENY("deny"),
    CANCEL("cancel"),
    DELETE("delete"),
    DONE("done"),
    THEME("theme"),
    JETBRAINS_TERMS_OF_SERVICE("jetbrains_terms_of_service"),
    HYPERSKILL_TERMS_OF_SERVICE("hyperskill_terms_of_service"),
    HELP_CENTER("help_center"),
    LOGOUT("logout"),
    DELETE_ACCOUNT("delete_account"),
    DELETE_ACCOUNT_NOTICE("delete_account_notice"),
    NOTIFICATIONS_SYSTEM_NOTICE("notifications_system_notice"),
    VIEW_FULL_PROFILE("view_full_profile"),
    SETTINGS("settings"),
    DAILY_STUDY_REMINDS("daily_study_reminds"),
    DAILY_STUDY_REMINDS_TIME("daily_study_reminds_time"),
    CONTINUE_TO_HYPERSKILL("continue_to_hyperskill"),
    CONTINUE("continue"),
    SIGN_IN("sign_in"),
    SIGN_UP("sign_up"),
    JETBRAINS_ACCOUNT("jetbrains_account"),
    GOOGLE("google"),
    GITHUB("github"),
    APPLE("apple"),
    CONTINUE_WITH_EMAIL("continue_with_email"),
    LOG_IN("log_in"),
    RESET_PASSWORD("reset_password"),
    CONTINUE_WITH_SOCIAL_NETWORKS("continue_with_social_networks")
}