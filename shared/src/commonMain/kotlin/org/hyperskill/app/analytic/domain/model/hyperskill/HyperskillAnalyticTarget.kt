package org.hyperskill.app.analytic.domain.model.hyperskill

enum class HyperskillAnalyticTarget(val targetName: String) {
    HOME("home"),
    TRACK("track"),
    STUDY_PLAN("study_plan"),
    LEADERBOARD("leaderboard"),
    PROFILE("profile"),
    DEBUG("debug"),
    SEND("send"),
    SKIP("skip"),
    INPUT_OUTPUT_INFO("input_output_info"),
    STEP_TEXT_DESCRIPTION("step_text_description"),
    STEP_THEORY_FEEDBACK_MODAL("step_theory_feedback_modal"),
    RESET("reset"),
    RUN("run"),
    ALLOW("allow"),
    DENY("deny"),
    OK("ok"),
    CANCEL("cancel"),
    DELETE("delete"),
    DONE("done"),
    YES("yes"),
    NO("no"),
    THEME("theme"),
    JETBRAINS_TERMS_OF_SERVICE("jetbrains_terms_of_service"),
    HYPERSKILL_TERMS_OF_SERVICE("hyperskill_terms_of_service"),
    REPORT_PROBLEM("report_a_problem"),
    SEND_FEEDBACK("send_feedback"),
    DELETE_ACCOUNT("delete_account"),
    DELETE_ACCOUNT_NOTICE("delete_account_notice"),
    RATE_US_IN_APP_STORE("rate_us_in_app_store"),
    RATE_US_IN_PLAY_STORE("rate_us_in_play_store"),
    SIGN_OUT_NOTICE("sign_out_notice"),
    NOTIFICATIONS_SYSTEM_NOTICE("notifications_system_notice"),
    VIEW_FULL_PROFILE("view_full_profile"),
    SETTINGS("settings"),
    DAILY_STUDY_REMINDS("daily_study_reminds"),
    DAILY_STUDY_REMINDS_TIME("daily_study_reminds_time"),
    DAILY_NOTIFICATION("daily_notification"),
    CONTINUE("continue"),
    RELOAD("reload"),
    DEADLINE_RELOAD("deadline_reload"),
    START_PRACTICING("start_practicing"),
    SIGN_IN("sign_in"),
    SIGN_UP("sign_up"),
    SIGN_OUT("sign_out"),
    JETBRAINS_ACCOUNT("jetbrains_account"),
    GOOGLE("google"),
    GITHUB("github"),
    APPLE("apple"),
    CONTINUE_WITH_EMAIL("continue_with_email"),
    LOG_IN("log_in"),
    RESET_PASSWORD("reset_password"),
    CONTINUE_WITH_SOCIAL_NETWORKS("continue_with_social_networks"),
    SEE_HINT("see_hint"),
    SEE_NEXT_HINT("see_next_hint"),
    REPORT("report"),
    REPORT_HINT_NOTICE("report_hint_notice"),
    REFRESH("refresh"),
    GO_BACK("go_back"),
    GO_TO_HOME_SCREEN("go_to_home_screen"),
    CONTINUE_WITH_NEXT_TOPIC("continue_with_next_topic"),
    DAILY_STEP_COMPLETED_MODAL("daily_step_completed_modal"),
    PROBLEMS_LIMIT_REACHED_MODAL("problems_limit_reached_modal"),
    PARSONS_PROBLEM_ONBOARDING_MODAL("parsons_problem_onboarding_modal"),
    FILL_BLANKS_INPUT_MODE_ONBOARDING_MODAL("fill_blanks_input_mode_onboarding_modal"),
    FILL_BLANKS_SELECT_MODE_ONBOARDING_MODAL("fill_blanks_select_mode_onboarding_modal"),
    GPT_CODE_GENERATION_WITH_ERRORS_ONBOARDING_MODAL("gpt_code_generation_with_errors_onboarding_modal"),
    TOPIC_COMPLETED_MODAL("topic_completed_modal"),
    CLOSE("close"),
    STREAK("streak"),
    PROGRESS("progress"),
    SEARCH("search"),
    GET_STREAK_FREEZE("get_streak_freeze"),
    STREAK_FREEZE_ICON("streak_freeze_icon"),
    STREAK_FREEZE_MODAL("streak_freeze_modal"),
    GET_IT("get_it"),
    CONTINUE_LEARNING("continue_learning"),
    STAGE_IMPLEMENT_UNSUPPORTED_MODAL("stage_implement_unsupported_modal"),
    RETRY("retry"),
    SECTION("section"),
    ACTIVITY("activity"),
    PROJECT("project"),
    THEORY("theory"),
    SELECT_THIS_TRACK("select_this_track"),
    SELECT_THIS_PROJECT("select_this_project"),
    STREAK_RECOVERY_MODAL("streak_recovery_modal"),
    RESTORE_STREAK("restore_streak"),
    NO_THANKS("no_thanks"),
    STAGE_COMPLETED_MODAL("stage_completed_modal"),
    PROJECT_COMPLETED_MODAL("project_completed_modal"),
    GO_TO_STUDY_PLAN("go_to_study_plan"),
    CHANGE_TRACK("change_track"),
    CHANGE_PROJECT("change_project"),
    BADGES_VISIBILITY_BUTTON("badges_visibility_button"),
    BADGE_CARD("badges_card"),
    BADGE_MODAL("badge_modal"),
    EARNED_BADGE_MODAL("earned_badge_modal"),
    ALLOW_NOTIFICATIONS("allow_notifications"),
    KEEP_LEARNING("keep_learning"),
    START_LEARNING("start_learning"),
    NOT_NOW("not_now"),
    FULL_SCREEN_CODE_EDITOR("full_screen_code_editor"),
    CODE_INPUT_ACCESSORY_BUTTON("code_input_accessory_button"),
    GPT_GENERATED_CODE_WITH_ERRORS("gpt_generated_code_with_errors"),
    SHARE_YOUR_STREAK("share_your_streak"),
    SHARE_STREAK_MODAL("share_streak_modal"),
    SHARE("share"),
    LINK("link"),
    COLLECT_REWARD("collect_reward"),
    DAY("day"),
    WEEK("week"),
    LEADERBOARD_ITEM("leaderboard_item"),
    TOPIC("topic"),
    DAILY_STUDY_REMINDERS_HOUR_INTERVAL_PICKER_MODAL("daily_study_reminders_hour_interval_picker_modal"),
    CONFIRM("confirm"),
    HOME_SCREEN_QUICK_ACTION("home_screen_quick_action"),
    REQUEST_REVIEW_MODAL("request_review_modal"),
    WRITE_A_REQUEST("write_a_request"),
    MAYBE_LATER("maybe_later"),
    CHOICE("choice"),
    SOLVE_ON_THE_WEB_VERSION("solve_on_the_web_version"),
    ACTIVE_SUBSCRIPTION_DETAILS("active_subscription_details"),
    SUBSCRIPTION_SUGGESTION_DETAILS("subscription_suggestion_details"),
    BUY_SUBSCRIPTION("buy_subscription"),
    CONTINUE_WITH_LIMITS("continue_with_limits"),
    UNLOCK_UNLIMITED_PROBLEMS("unlock_unlimited_problems"),
    MANAGE_SUBSCRIPTION("manage_subscription"),
    RENEW_SUBSCRIPTION("renew_subscription"),
    HYPERSKILL_TERMS_OF_SERVICE_AND_PRIVACY_POLICY("hyperskill_terms_of_service_and_privacy_policy")
}