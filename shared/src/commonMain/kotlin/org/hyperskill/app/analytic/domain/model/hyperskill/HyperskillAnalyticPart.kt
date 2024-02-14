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
    CHALLENGE_CARD("challenge_card"),
    REPEAT_NEXT_TOPIC("repeat_next_topic"),
    REPEAT_TOPIC("repeat_topic"),
    STEP_HINTS("step_hints"),
    DAILY_STEP_COMPLETED_MODAL("daily_step_completed_modal"),
    TOPIC_COMPLETED_MODAL("topic_completed_modal"),
    PROBLEMS_LIMIT_REACHED_MODAL("problems_limit_reached_modal"),
    PROBLEMS_LIMIT_WIDGET("problems_limit_widget"),
    PARSONS_PROBLEM_ONBOARDING_MODAL("parsons_problem_onboarding_modal"),
    FILL_BLANKS_INPUT_MODE_ONBOARDING_MODAL("fill_blanks_input_mode_onboarding_modal"),
    FILL_BLANKS_SELECT_MODE_ONBOARDING_MODAL("fill_blanks_select_mode_onboarding_modal"),
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
    FULL_SCREEN_CODE_EDITOR("full_screen_code_editor"),
    CODE_EDITOR("code_editor"),
    SHARE_STREAK_MODAL("share_streak_modal"),
    LEADERBOARD_DAY_TAB("leaderboard_day_tab"),
    LEADERBOARD_WEEK_TAB("leaderboard_week_tab"),
    SEARCH_RESULTS("search_results"),
    DAILY_STUDY_REMINDERS_HOUR_INTERVAL_PICKER_MODAL("daily_study_reminders_hour_interval_picker_modal"),
    INTERVIEW_PREPARATION_WIDGET("interview_preparation_widget"),
    INTERVIEW_PREPARATION_COMPLETED_MODAL("interview_preparation_completed_modal"),
    REQUEST_REVIEW_MODAL("request_review_modal")
}