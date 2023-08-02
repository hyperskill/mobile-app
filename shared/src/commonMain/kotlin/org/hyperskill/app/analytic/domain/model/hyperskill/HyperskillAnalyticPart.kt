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
    DAILY_STEP_COMPLETED_MODAL("daily_step_completed_modal"),
    TOPIC_COMPLETED_MODAL("topic_completed_modal"),
    PROBLEMS_LIMIT_REACHED_MODAL("problems_limit_reached_modal"),
    MODAL("modal"),
    STREAK_WIDGET("streak_widget"),
    STREAK_FREEZE_MODAL("streak_freeze_modal"),
    STAGE_IMPLEMENT_UNSUPPORTED_MODAL("stage_implement_unsupported_modal"),
    STUDY_PLAN_SECTION("study_plan_section"),
    STUDY_PLAN_SECTION_ACTIVITIES("study_plan_section_activities"),
    PROJECTS_LIST("projects_list"),
    STREAK_RECOVERY_MODAL("streak_recovery_modal"),
    STAGE_COMPLETED_MODAL("stage_completed_modal"),
    PROJECT_COMPLETED_MODAL("project_completed_modal"),
    NEXT_LEARNING_ACTIVITY_WIDGET("next_learning_activity_widget")
}