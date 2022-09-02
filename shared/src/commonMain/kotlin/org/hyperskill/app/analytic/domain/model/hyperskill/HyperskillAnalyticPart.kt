package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticPart(val partName: String) {
    MAIN("main"),
    HEAD("head"),
    TABS("tabs"),
    NOTICE("notice"),
    ACTIONS("actions"),
    DESCRIPTION("description"),
    DELETE_ACCOUNT_NOTICE("delete_account_notice"),
    NOTIFICATIONS_SYSTEM_NOTICE("notifications_system_notice"),
    PROBLEM_OF_THE_DAY_CARD("problem_of_the_day_card"),
    DAILY_NOTIFICATIONS_NOTICE("daily_notifications_notice")
}