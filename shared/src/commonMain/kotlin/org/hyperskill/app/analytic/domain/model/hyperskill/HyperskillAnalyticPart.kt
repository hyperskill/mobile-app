package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticPart(val partName: String) {
    MAIN("main"),
    HEAD("head"),
    TABS("tabs"),
    NOTICE("notice"),
    REPORT_HINT_NOTICE("report_hint_notice"),
    NOTIFICATION("notification"),
    ACTIONS("actions"),
    DESCRIPTION("description"),
    DELETE_ACCOUNT_NOTICE("delete_account_notice"),
    SIGN_OUT_NOTICE("sign_out_notice"),
    NOTIFICATIONS_SYSTEM_NOTICE("notifications_system_notice"),
    PROBLEM_OF_THE_DAY_CARD("problem_of_the_day_card"),
    TOPICS_REPETITIONS_CARD("topics_repetitions_card"),
    REPEAT_NEXT_TOPIC("repeat_next_topic"),
    REPEAT_TOPIC("repeat_topic"),
    DAILY_NOTIFICATIONS_NOTICE("daily_notifications_notice"),
    STEP_HINTS("step_hints"),
    THEORY_TO_DISCOVER_NEXT("theory_to_discover_next"),
    DAILY_STEP_COMPLETED_MODAL("daily_step_completed_modal"),
    MODAL("modal"),
    TRACK_MODAL("track_modal"),
    STREAK_WIDGET("streak_widget"),
    STREAK_FREEZE_MODAL("streak_freeze_modal"),
    TOPIC_TO_LEARN_NEXT("topic_to_learn_next")
}