import Foundation
import shared

enum Strings {
    private static let sharedStrings = SharedResources.strings.shared

    // MARK: - General -

    enum General {
        static let connectionError = sharedStrings.connection_error.localized()
        static let done = sharedStrings.done.localized()
        static let yes = sharedStrings.yes.localized()
        static let no = sharedStrings.no.localized()
        static let cancel = sharedStrings.cancel.localized()
        static let ok = sharedStrings.ok.localized()
        static let later = sharedStrings.later.localized()
        static let goodJob = sharedStrings.good_job.localized()
        static let goToHomescreen = sharedStrings.go_to_homescreen.localized()
    }

    // MARK: - TabBar -

    enum TabBar {
        static let home = sharedStrings.tab_bar_home_title.localized()
        static let track = sharedStrings.tab_bar_track_title.localized()
        static let profile = sharedStrings.tab_bar_profile_title.localized()
        static let debug = sharedStrings.tab_bar_debug_title.localized()
    }

    // MARK: - Auth -

    enum Auth {
        // MARK: Social

        enum Social {
            static let logInTitle = sharedStrings.auth_log_in_title.localized()
            static let signUpTitle = sharedStrings.auth_sign_up_title.localized()
            static let jetBrainsAccount = sharedStrings.auth_jetbrains_account_text.localized()
            static let googleAccount = sharedStrings.auth_google_account_text.localized()
            static let gitHubAccount = sharedStrings.auth_github_account_text.localized()
            static let appleAccount = sharedStrings.auth_apple_account_text.localized()
            static let emailText = sharedStrings.auth_email_text.localized()
        }

        // MARK: Credentials

        enum Credentials {
            static let socialText = sharedStrings.auth_credentials_social_text.localized()
            static let resetPassword = sharedStrings.auth_credentials_reset_password_text.localized()
            static let logIn = sharedStrings.auth_credentials_log_in_text.localized()
            static let emailPlaceholder = sharedStrings.auth_credentials_email_placeholder.localized()
            static let passwordPlaceholder = sharedStrings.auth_credentials_password_placeholder.localized()
        }

        // MARK: NewUserPlaceholder

        enum NewUserPlaceholder {
            static let title = sharedStrings.placeholder_new_user_title.localized()
            static let subtitle = sharedStrings.placeholder_new_user_text.localized()

            static let startLearningButton = sharedStrings.placeholder_new_user_start_track_button_text.localized()

            enum TrackSelectionStatus {
                static let error = sharedStrings.placeholder_new_user_start_track_error_message.localized()
                static let success = sharedStrings.placeholder_new_user_start_track_success_message.localized()
            }
        }
    }

    // MARK: - Step -

    enum Step {
        static let startPracticing = sharedStrings.step_start_practicing_text.localized()
        static let theory = sharedStrings.step_theory_text.localized()
    }

    // MARK: - StepQuiz -

    enum StepQuiz {
        static let quizStatusCorrect = sharedStrings.step_quiz_status_correct_text.localized()
        static let quizStatusWrong = sharedStrings.step_quiz_status_wrong_text.localized()
        static let quizStatusEvaluation = sharedStrings.step_quiz_status_evaluation_text.localized()
        static let quizStatusLoading = sharedStrings.step_quiz_status_loading_text.localized()
        static let feedbackTitle = sharedStrings.step_quiz_feedback_title.localized()
        static let continueButton = sharedStrings.step_quiz_continue_button_text.localized()
        static let retryButton = sharedStrings.step_quiz_retry_button_text.localized()
        static let sendButton = sharedStrings.step_quiz_send_button_text.localized()
        static let checkingButton = sharedStrings.step_quiz_checking_button_text.localized()
        static let discussionsButton = sharedStrings.step_quiz_discussions_button_text.localized()
        static let unsupportedText = sharedStrings.step_quiz_unsupported_quiz_text.localized()

        enum Hints {
            static let showButton = sharedStrings.step_quiz_hints_show_button_text.localized()
            static let reportButton = sharedStrings.step_quiz_hints_report_button_text.localized()
            static let helpfulQuestion = sharedStrings.step_quiz_hints_helpful_question_text.localized()
            static let seeNextHint = sharedStrings.step_quiz_hints_see_next_hint.localized()
            static let lastHint = sharedStrings.step_quiz_hints_last_hint_text.localized()
            static let reportAlertTitle = sharedStrings.step_quiz_hints_report_alert_title.localized()
            static let reportAlertText = sharedStrings.step_quiz_hints_report_alert_text.localized()
            static let showMore = sharedStrings.step_quiz_hints_show_more_text.localized()
        }

        enum ProblemOfDaySolvedModal {
            static let title = sharedStrings.step_quiz_problem_of_day_solved_modal_title.localized()
            static let text = sharedStrings.step_quiz_problem_of_day_solved_modal_text.localized()
        }

        enum TopicCompletedModal {
            static let continueWithNextTopicButtonText =
                sharedStrings.step_quiz_topic_completed_continue_with_next_topic_button_text.localized()
        }
    }

    // MARK: - StepQuizChoice -

    enum StepQuizChoice {
        static let singleChoiceTitle = sharedStrings.step_quiz_choice_single_choice_title.localized()
        static let multipleChoiceTitle = sharedStrings.step_quiz_choice_multiple_choice_title.localized()
    }

    // MARK: - StepQuizCode -

    enum StepQuizCode {
        static let title = sharedStrings.step_quiz_code_title.localized()

        static let detailsTitle = sharedStrings.step_quiz_code_details.localized()
        static let sampleInputTitleResource = sharedStrings.step_quiz_code_detail_sample_input_title
        static let sampleOutputTitleResource = sharedStrings.step_quiz_code_detail_sample_output_title
        static let timeLimitTitle = sharedStrings.step_quiz_code_detail_execution_time_limit_title.localized()
        static let memoryLimitTitle = sharedStrings.step_quiz_code_detail_execution_memory_limit_title.localized()
        static let memoryLimitValueResource = sharedStrings.step_quiz_code_detail_execution_memory_limit_value

        static let runSolutionButton = sharedStrings.step_quiz_code_run_solution_button_text.localized()
        static let fullScreenDetailsTab = sharedStrings.step_quiz_code_full_screen_details_tab.localized()
        static let fullScreenCodeTab = sharedStrings.step_quiz_code_full_screen_code_tab.localized()
        static let emptyLang = sharedStrings.step_quiz_code_empty_lang.localized()
        static let reset = sharedStrings.step_quiz_code_reset.localized()
    }

    // MARK: - StepQuizSQL -

    enum StepQuizSQL {
        static let title = sharedStrings.step_quiz_sql_title.localized()
    }

    // MARK: - StepQuizTable -

    enum StepQuizTable {
        static let singleChoicePrompt = sharedStrings.step_quiz_table_single_choice.localized()
        static let multipleChoicePrompt = sharedStrings.step_quiz_table_multiple_choice.localized()
        static let confirmButton = sharedStrings.step_quiz_table_confirm_choice.localized()
    }

    // MARK: - Placeholder -

    enum Placeholder {
        static let networkErrorTitle = sharedStrings.placeholder_network_error_title.localized()
        static let networkErrorButtonText = sharedStrings.placeholder_network_error_button_text.localized()
    }

    // MARK: - StepQuizSorting -

    enum StepQuizSorting {
        static let title = sharedStrings.step_quiz_sorting_title.localized()
    }

    // MARK: - StepQuizString -

    enum StepQuizString {
        static let placeholder = sharedStrings.step_quiz_text_field_hint.localized()
    }

    // MARK: - Home -

    enum Home {
        static let title = sharedStrings.home_title.localized()
        static let keepPracticing = sharedStrings.home_keep_practicing_text.localized()
        static let topicsToDiscoverNextTitle = sharedStrings.home_topics_to_discover_next_title.localized()
    }

    // MARK: - Track -

    enum Track {
        static let title = sharedStrings.track_title.localized()

        enum Header {
            static let learningNow = sharedStrings.track_learning_now_text.localized()
        }

        enum Progress {
            static let title = sharedStrings.track_progress_block_title.localized()
            static let timeToComplete = sharedStrings.track_time_to_complete_text.localized()
            static let completedGraduateProject = sharedStrings.track_completed_graduate_project_text.localized()
            static let completedTopics = sharedStrings.track_completed_topics_text.localized()
            static let appliedCoreTopics = sharedStrings.track_applied_core_topics_text.localized()
        }

        enum TopicsToDiscoverNext {
            static let title = sharedStrings.track_topics_to_discover_next_block_title.localized()
            static let learnNextBadge = sharedStrings.topics_widget_learn_next_badge.localized()
        }

        enum About {
            static let title = sharedStrings.track_about_text.localized()
            static let continueInWebButton = sharedStrings.track_continue_in_web_text.localized()
        }
    }

    // MARK: - StudyPlan -

    enum StudyPlan {
        enum IdeRequiredModal {
            static let title = sharedStrings.study_plan_ide_required_modal_title.localized()
            static let text = sharedStrings.study_plan_ide_required_modal_text.localized()
        }
    }

    // MARK: - Profile -

    enum Profile {
        static let title = sharedStrings.profile_title.localized()
        static let aboutMe = sharedStrings.profile_about_me_text.localized()
        static let livesIn = sharedStrings.profile_lives_in_text.localized()
        static let speaks = sharedStrings.profile_speaks_text.localized()
        static let bio = sharedStrings.profile_bio_text.localized()
        static let experience = sharedStrings.profile_experience_text.localized()
        static let viewFullVersionButton = sharedStrings.profile_view_full_version_button_text.localized()
        static let roleStaff = sharedStrings.profile_role_staff_text.localized()
        static let roleLearner = sharedStrings.profile_role_learner_text.localized()

        enum Statistics {
            static let passedProjects = sharedStrings.profile_statistics_block_passed_projects_title.localized()
            static let passedTracks = sharedStrings.profile_statistics_block_passed_tracks_title.localized()
            static let hypercoinsBalance = sharedStrings.profile_statistics_block_hypercoins_balance_title.localized()
        }

        enum DailyStudyReminders {
            static let title = sharedStrings.profile_daily_study_reminders_text.localized()
            static let schedule = sharedStrings.profile_daily_study_reminders_schedule_text.localized()
        }
    }

    // MARK: - Settings -

    enum Settings {
        static let title = sharedStrings.settings_title.localized()
        static let appearance = sharedStrings.settings_appearance.localized()
        static let about = sharedStrings.settings_about.localized()
        static let termsOfService = sharedStrings.settings_terms_of_service.localized()
        static let privacyPolicy = sharedStrings.settings_privacy_policy.localized()
        static let reportProblem = sharedStrings.settings_report_problem.localized()
        static let sendFeedback = sharedStrings.settings_send_feedback.localized()
        static let version = sharedStrings.settings_version.localized()
        static let rateApplication = sharedStrings.settings_rate_application.localized()
        static let signOut = sharedStrings.settings_sign_out.localized()
        static let signOutAlertTitle = sharedStrings.settings_sign_out_dialog_title.localized()
        static let signOutAlertMessage = sharedStrings.settings_sign_out_dialog_explanation.localized()
        static let deleteAccount = sharedStrings.settings_delete_account.localized()
        static let deleteAccountAlertTitle = sharedStrings.settings_account_deletion_dialog_title.localized()
        static let deleteAccountAlertMessage = sharedStrings.settings_account_deletion_dialog_explanation.localized()
        static let deleteAccountAlertDeleteButton =
            sharedStrings.settings_account_deletion_dialog_delete_button_text.localized()
        static let termsOfServiceURL = sharedStrings.settings_terms_of_service_url.localized()
        static let privacyPolicyURL = sharedStrings.settings_privacy_policy_url.localized()
        static let reportProblemURL = sharedStrings.settings_report_problem_url.localized()

        enum Theme {
            static let title = sharedStrings.settings_theme.localized()

            static let light = sharedStrings.settings_theme_light.localized()
            static let dark = sharedStrings.settings_theme_dark.localized()
            static let system = sharedStrings.settings_theme_system.localized()
        }
    }

    // MARK: - DebugMenu -

    enum DebugMenu {
        static let title = sharedStrings.debug_menu_title.localized()
        static let applySettingsButton = sharedStrings.debug_menu_apply_settings_button_text.localized()

        enum API {
            static let headerTitle = sharedStrings.debug_menu_section_api_header_title.localized()
        }

        enum StepNavigation {
            static let headerTitle = sharedStrings.debug_menu_section_step_navigation_header_title.localized()
            static let textFieldTitle = sharedStrings.debug_menu_section_step_navigation_text_input_title.localized()
            static let buttonTitle = sharedStrings.debug_menu_section_step_navigation_button_text.localized()
        }

        enum StageImplement {
            static let headerTitle = sharedStrings.debug_menu_section_stage_implement_header_title.localized()
            static let projectIdTextFieldTitle =
              sharedStrings.debug_menu_section_stage_implement_project_id_text_input_title.localized()
            static let stageIdTextFieldTitle =
              sharedStrings.debug_menu_section_stage_implement_stage_id_text_input_title.localized()
            static let buttonTitle = sharedStrings.debug_menu_section_stage_implement_button_text.localized()
        }

        enum RestartApplication {
            enum Alert {
                static let title = sharedStrings.debug_menu_restart_application_alert_title_ios.localized()
                static let message = sharedStrings.debug_menu_restart_application_alert_message_ios.localized()
            }

            enum LocalNotification {
                static let title = sharedStrings.debug_menu_restart_application_local_notification_title_ios.localized()
                static let body = sharedStrings.debug_menu_restart_application_local_notification_body_ios.localized()
            }
        }
    }

    // MARK: - Streak -

    enum Streak {
        static let solvingProblemText = sharedStrings.streak_solving_problem_text.localized()
        static let keepSolvingProblemsText = sharedStrings.streak_keep_solving_problems_text.localized()
        static let previousFiveDaysText = sharedStrings.streak_previous_five_days_text.localized()
        static let todayText = sharedStrings.streak_today_text.localized()
        static let getOneDayStreakFreeze = sharedStrings.streak_get_one_day_streak_freeze.localized()
        static let youHaveOneDayStreakFreeze = sharedStrings.streak_you_have_one_day_streak_freeze.localized()

        enum FreezeModal {
            static let canBuyTitle = sharedStrings.streak_freeze_modal_can_buy_title.localized()
            static let alreadyHaveTitle = sharedStrings.streak_freeze_modal_already_have_title.localized()
            static let notEnoughGemsTitle = sharedStrings.streak_freeze_modal_not_enough_gems_title.localized()
            static let text = sharedStrings.streak_freeze_modal_text.localized()
            static let oneDayStreakFreeze = sharedStrings.streak_freeze_modal_one_day_streak_freeze.localized()
            static let youHaveOneDayStreakFreeze =
                sharedStrings.streak_freeze_modal_you_have_one_day_streak_freeze.localized()
            static let continueLearning = sharedStrings.streak_freeze_modal_continue_learning.localized()
            static let getItForGemsResource = sharedStrings.streak_freeze_modal_get_it_for_gems
            static let boughtError = sharedStrings.streak_freeze_bought_error.localized()
            static let boughtSuccess = sharedStrings.streak_freeze_bought_success.localized()
        }
    }

    // MARK: - ProblemOfDay -

    enum ProblemOfDay {
        static let titleUncompleted = sharedStrings.problem_of_day_title_uncompleted.localized()
        static let titleCompleted = sharedStrings.problem_of_day_title_completed.localized()
        static let noProblemsToSolve = sharedStrings.problem_of_day_no_problems_to_solve.localized()
        static let solveARandomProblem = sharedStrings.problem_of_day_solve_a_random_problem.localized()
        static let getBack = sharedStrings.problem_of_day_get_back.localized()
        static let nextProblemIn = sharedStrings.problem_of_day_next_problem_in.localized()
    }

    // MARK: - TopicsRepetitions -

    enum TopicsRepetitions {
        enum Card {
            static let titleUncompleted = sharedStrings.topics_repetitions_card_title_uncompleted.localized()
            static let textCompleted = sharedStrings.topics_repetitions_card_text_completed.localized()
        }

        enum StatusBlock {
            static let tryToRecallText = sharedStrings.topics_repetitions_try_to_recall_text.localized()
            static let goodJobText = sharedStrings.topics_repetitions_good_job_text.localized()
            static let allTopicsRepeated = sharedStrings.topics_repetitions_all_topics_repeated_text.localized()
        }

        enum Chart {
            static let title = sharedStrings.topics_repetitions_chart_title.localized()
            static let description = sharedStrings.topics_repetitions_chart_description.localized()
        }

        enum RepeatBlock {
            static let title = sharedStrings.topics_repetitions_repeat_block_title.localized()
            static let currentTrack = sharedStrings.topics_repetitions_repeat_block_current_track.localized()
            static let otherTracks = sharedStrings.topics_repetitions_repeat_block_other_tracks.localized()
        }

        enum InfoBlock {
            static let title = sharedStrings.topics_repetitions_info_block_title.localized()
            static let description = sharedStrings.topics_repetitions_info_block_description.localized()
        }
    }

    // MARK: - Onboarding -

    enum Onboarding {
        static let title = sharedStrings.onboarding_title.localized()
        static let text = sharedStrings.onboarding_text.localized()
        static let signIn = sharedStrings.onboarding_sign_in.localized()
        static let signUp = sharedStrings.onboarding_sign_up.localized()
    }
}
