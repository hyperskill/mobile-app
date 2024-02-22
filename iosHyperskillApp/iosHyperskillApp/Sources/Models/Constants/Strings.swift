import Foundation
import shared

enum Strings {
    private static let sharedStrings = SharedResources.strings.shared

    // MARK: - Common -

    enum Common {
        static let appName = sharedStrings.ios_app_name.localized()
        static let connectionError = sharedStrings.connection_error.localized()
        static let done = sharedStrings.done.localized()
        static let yes = sharedStrings.yes.localized()
        static let no = sharedStrings.no.localized()
        static let cancel = sharedStrings.cancel.localized()
        static let ok = sharedStrings.ok.localized()
        static let later = sharedStrings.later.localized()
        static let goodJob = sharedStrings.good_job.localized()
        static let goToTraining = sharedStrings.go_to_training.localized()
        static let goToStudyPlan = sharedStrings.go_to_study_plan.localized()
        static let progress = sharedStrings.progress.localized()
        static let completed = sharedStrings.completed.localized()
    }

    // MARK: - Badge -

    enum Badge {
        static let ideRequired = sharedStrings.badge_ide_required_text.localized()
        static let current = sharedStrings.badge_current_text.localized()
        static let solveUnlimited = sharedStrings.badge_solve_unlimited_text.localized()
        static let repeatUnlimited = sharedStrings.badge_repeat_unlimited_text.localized()
        static let selected = sharedStrings.badge_selected_text.localized()
        static let bestRating = sharedStrings.badge_best_rating_text.localized()
        static let fastestToComplete = sharedStrings.badge_fastest_to_complete_text.localized()
        static let beta = sharedStrings.badge_beta_text.localized()
        static let firstTimeOffer = sharedStrings.badge_first_time_offer_text.localized()
    }

    // MARK: - TabBar -

    enum TabBar {
        static let home = sharedStrings.tab_bar_training_title.localized()
        static let studyPlan = sharedStrings.tab_bar_study_plan_title.localized()
        static let leaderboard = sharedStrings.tab_bar_leaderboard_title.localized()
        static let profile = sharedStrings.tab_bar_profile_title.localized()
        static let debug = sharedStrings.tab_bar_debug_title.localized()
    }

    // MARK: - Auth -

    enum Auth {
        // MARK: Social

        enum Social {
            static let logInTitle = sharedStrings.auth_log_in_title.format(args_: [Common.appName]).localized()
            static let signUpTitle = sharedStrings.auth_sign_up_title.format(args_: [Common.appName]).localized()
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

        static let stepTextHeaderTitle = sharedStrings.step_quiz_step_text_header_title.localized()

        enum ResetCodeAlert {
            static let title = sharedStrings.reset_code_dialog_title.localized()
            static let text = sharedStrings.reset_code_dialog_explanation.localized()
        }

        enum Hints {
            static let showButton = sharedStrings.step_quiz_hints_show_button_text.localized()
            static let reportButton = sharedStrings.step_quiz_hints_report_button_text.localized()
            static let helpfulQuestion = sharedStrings.step_quiz_hints_helpful_question_text.localized()
            static let seeNextHint = sharedStrings.step_quiz_hints_see_next_hint.localized()
            static let lastHint = sharedStrings.step_quiz_hints_last_hint_text.localized()
            static let reportAlertTitle = sharedStrings.step_quiz_hints_report_alert_title.localized()
            static let reportAlertText = sharedStrings.step_quiz_hints_report_alert_text.localized()
            static let showMore = sharedStrings.step_quiz_hints_show_more_text.localized()
            static let copy = sharedStrings.step_quiz_hints_copy.localized()
        }

        enum ProblemOfDaySolvedModal {
            static let title = sharedStrings.step_quiz_problem_of_day_solved_modal_title.localized()
            static let text = sharedStrings.step_quiz_problem_of_day_solved_modal_text.localized()

            static let shareStreakButton =
              sharedStrings.step_quiz_problem_of_day_solved_modal_share_streak_button_text.localized()
        }

        enum TopicCompletedModal {
            static let continueWithNextTopicButtonText =
                sharedStrings.step_quiz_topic_completed_continue_with_next_topic_button_text.localized()
        }

        enum ProblemsLimitReachedModal {
            static let title = sharedStrings.problems_limit_reached_modal_title.localized()
        }

        enum ShareStreakModal {
            static let title = sharedStrings.share_streak_modal_title.localized()
            static let shareButton = sharedStrings.share_streak_modal_share_button_text.localized()
            static let noThanksButton = sharedStrings.share_streak_modal_no_thanks_button_text.localized()

            static let sharingText = sharedStrings.share_streak_sharing_text.localized()
            static let sharingURL = sharedStrings.share_streak_sharing_url.localized()
        }

        enum ProblemOnboardingModal {
            static let header = sharedStrings.step_quiz_problem_onboarding_modal_header.localized()
            static let title = sharedStrings.step_quiz_problem_onboarding_modal_title.localized()

            static let parsonsDescription =
              sharedStrings.step_quiz_problem_onboarding_modal_parsons_description.localized()
            static let fillBlanksDescription =
              sharedStrings.step_quiz_problem_onboarding_modal_fill_blanks_description.localized()
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

        static let runSolutionButton = sharedStrings.step_quiz_code_run_solution_button_text.localized()
        static let fullScreenDetailsTab = sharedStrings.step_quiz_code_full_screen_details_tab.localized()
        static let fullScreenCodeTab = sharedStrings.step_quiz_code_full_screen_code_tab.localized()
        static let emptyLang = sharedStrings.step_quiz_code_empty_lang.localized()
        static let reset = sharedStrings.step_quiz_code_reset.localized()

        static let codeEditorTitle = sharedStrings.step_quiz_code_editor_title.localized()
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

    // MARK: - StepQuizParsons -

    enum StepQuizParsons {
        static let tab = sharedStrings.step_quiz_parsons_tab_text.localized()
    }

    // MARK: - StepQuizFillBlanks-

    enum StepQuizFillBlanks {
        static let title = sharedStrings.step_quiz_fill_blanks_title.localized()
    }

    // MARK: - StageImplement -

    enum StageImplement {
        enum UnsupportedModal {
            static let title = sharedStrings.stage_implement_unsupported_modal_title.localized()
            static let description = sharedStrings.stage_implement_unsupported_modal_description.localized()
        }

        enum StageCompletedModal {
            static let description = sharedStrings.stage_completed_modal_text.localized()
            static let awardDescription = sharedStrings.stage_completed_modal_stage_reward_text.localized()
        }

        enum ProjectCompletedModal {
            static let title = sharedStrings.project_completed_modal_title.localized()
            static let description = sharedStrings.project_completed_modal_text.localized()
            static let awardDescription = sharedStrings.project_completed_modal_project_reward_text.localized()
        }
    }

    // MARK: - Home -

    enum Home {
        static let title = sharedStrings.home_title.localized()
        static let keepPracticing = sharedStrings.home_keep_practicing_text.localized()

        static let solveUnlimited = sharedStrings.home_solve_unlimited.localized()
        static let repeatUnlimited = sharedStrings.home_repeat_unlimited.localized()
    }

    // MARK: - Challenge widget -

    enum ChallengeWidget {
        static let networkError = sharedStrings.challenge_widget_network_error_text.localized()
    }

    // MARK: - Users questionnaire widget -

    enum UsersQuestionnaireWidget {
        static let title = sharedStrings.users_questionnaire_widget_title.localized()
    }

    // MARK: - Interview Preparation -

    // MARK: Widget

    enum InterviewPreparationWidget {
        static let title = sharedStrings.interview_preparation_widget_title.localized()
        static let networkError = sharedStrings.interview_preparation_widget_network_error_text.localized()
    }

    // MARK: Onboarding

    enum InterviewPreparationOnboarding {
        static let navigationTitle = sharedStrings.interview_preparation_onboarding_screen_title.localized()

        static let title = sharedStrings.interview_preparation_onboarding_title.localized()
        static let description = sharedStrings.interview_preparation_onboarding_description.localized()
        static let callToActionButton = sharedStrings.interview_preparation_onboarding_go_to_first_problem.localized()
    }

    // MARK: Completed modal

    enum InterviewPreparationCompletedModal {
        static let title = sharedStrings.interview_preparation_finished_modal_title.localized()
        static let description = sharedStrings.interview_preparation_finished_modal_description.localized()
    }

    // MARK: - Topics widget -

    enum TopicsWidget {
        static let learnNextBadge = sharedStrings.topics_widget_learn_next_badge.localized()
    }

    // MARK: - StudyPlan -

    enum StudyPlan {
        static let title = sharedStrings.study_plan_title.localized()
        static let activitiesError = sharedStrings.study_plan_activities_error_text.localized()
    }

    // MARK: - Leaderboard -

    enum Leaderboard {
        static let title = sharedStrings.leaderboard_title.localized()
        static let tabDay = sharedStrings.leaderboard_tab_day_title.localized()
        static let tabWeek = sharedStrings.leaderboard_tab_week_title.localized()
        static let placeholderEmptyDescription = sharedStrings.leaderboard_placeholder_empty_description.localized()
        static let placeholderErrorDescription = sharedStrings.leaderboard_placeholder_error_description.localized()
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

        enum Badges {
            static let title = sharedStrings.profile_badges_title.localized()
            static let showAll = sharedStrings.profile_badges_show_all.localized()
            static let showLess = sharedStrings.profile_badges_show_less.localized()
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

        static let rateInAppStore = sharedStrings.settings_rate_in_app_store.localized()
        static let rateInAppStoreURL = sharedStrings.settings_rate_in_app_store_url.localized()

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

        enum RecoverModal {
            static let title = sharedStrings.streak_recovery_modal_title.localized()
            static let warning = sharedStrings.streak_recovery_modal_warning.localized()
            static let restoreStreak = sharedStrings.streak_recovery_modal_restore_streak.localized()
            static let noThanks = sharedStrings.streak_recovery_modal_no_thanks.localized()
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

    // MARK: - Welcome -

    enum Welcome {
        static let title = sharedStrings.ios_onboarding_title.localized()
        static let text = sharedStrings.onboarding_text.localized()
        static let primaryButton = sharedStrings.onboarding_primary_button_text.localized()
        static let secondaryButton = sharedStrings.onboarding_secondary_button_text.localized()
    }

    // MARK: - NotificationsOnboarding -

    enum NotificationsOnboarding {
        static let title = sharedStrings.notifications_onboarding_title_new.localized()

        static let dailyStudyRemindersIntervalPrefix =
          sharedStrings.notifications_onboarding_daily_study_reminders_interval_prefix.localized()

        static let buttonPrimary = sharedStrings.notifications_onboarding_button_allow.localized()
        static let buttonSecondary = sharedStrings.notifications_onboarding_button_not_now.localized()
    }

    // MARK: - FirstProblemOnboarding -

    enum FirstProblemOnboarding {
        static let networkError = sharedStrings.first_problem_onboarding_network_error.localized()
    }

    // MARK: - ProjectSelectionList -

    enum ProjectSelectionList {
        static let title = sharedStrings.projects_list_toolbar_title.localized()

        enum List {
            static let description = sharedStrings.projects_list_description.localized()

            static let recommendedProjectsTitle = sharedStrings.projects_list_recommended_projects_title.localized()

            static let projectGraduateTitle = sharedStrings.projects_list_project_graduate_title.localized()

            enum Category {
                static let easyTitle = sharedStrings.projects_list_easy_category_title.localized()
                static let easyDescription = sharedStrings.projects_list_easy_category_description.localized()

                static let mediumTitle = sharedStrings.projects_list_medium_category_title.localized()
                static let mediumDescription = sharedStrings.projects_list_medium_category_description.localized()

                static let hardTitle = sharedStrings.projects_list_hard_category_title.localized()
                static let hardDescription = sharedStrings.projects_list_hard_category_description.localized()

                static let nightmareTitle = sharedStrings.projects_list_nightmare_category_title.localized()
                static let nightmareDescription = sharedStrings.projects_list_nightmare_category_description.localized()
            }
        }
    }

    // MARK: - ProjectSelectionDetails -

    enum ProjectSelectionDetails {
        static let learningOutcomesTitle = sharedStrings.project_selection_details_learning_outcomes_title.localized()
        static let projectOverviewTitle = sharedStrings.project_selection_details_project_overview_title.localized()
        static let providedByTitle = sharedStrings.project_selection_details_provided_by_title.localized()

        static let selectionSuccessMessage =
            sharedStrings.project_selection_details_project_selection_success_message.localized()
        static let selectionErrorMessage =
            sharedStrings.project_selection_details_project_selection_error_message.localized()

        static let callToActionButtonTitle = sharedStrings.project_selection_details_cta_button_title.localized()
    }

    // MARK: - TrackSelectionList -

    enum TrackSelectionList {
        static let navigationTitle = sharedStrings.track_selection_list_toolbar_title.localized()

        static let title = sharedStrings.placeholder_new_user_title.localized()
        static let subtitle = sharedStrings.placeholder_new_user_text.localized()
    }

    // MARK: - TrackSelectionDetails -

    enum TrackSelectionDetails {
        static let callToActionButtonTitle = sharedStrings.track_selection_details_select_button.localized()

        static let overviewTitle = sharedStrings.track_selection_details_overview_title.localized()
        static let certificateText = sharedStrings.track_selection_details_certificate_text.localized()

        static let selectionSuccessMessage = sharedStrings.track_selection_details_selection_succeed.localized()
        static let selectionErrorMessage = sharedStrings.track_selection_details_selection_failed.localized()

        static let mainProviderTitle = sharedStrings.track_selection_details_main_provider_title.localized()
        static let otherProvidersTitle = sharedStrings.track_selection_details_other_providers_title.localized()
    }

    // MARK: - ProgressScreen -

    enum ProgressScreen {
        static let navigationTitle = sharedStrings.progress_screen_title.localized()

        enum Track {
            static let completedTopics = sharedStrings.progress_screen_completed_topics.localized()
            static let appliedCoreTopics = sharedStrings.progress_screen_applied_core_topics.localized()
            static let timeToCompleteTrack = sharedStrings.progress_screen_time_to_complete_track.localized()
            static let completedGraduateProject = sharedStrings.progress_screen_completed_graduate_project.localized()
            static let changeTrack = sharedStrings.progress_screen_change_track.localized()
        }

        enum Project {
            static let timeToCompleteProject = sharedStrings.progress_screen_time_to_complete_project.localized()
            static let stages = sharedStrings.progress_screen_stages.localized()
            static let changeProject = sharedStrings.progress_screen_change_project.localized()
        }
    }

    // MARK: - Search -

    enum Search {
        static let title = sharedStrings.search_title.localized()

        static let placeholderEmptyTitle = sharedStrings.search_placeholder_empty_title.localized()
        static let placeholderEmptySubtitle = sharedStrings.search_placeholder_empty_subtitle.localized()

        static let placeholderSuggestionsTitle = sharedStrings.search_placeholder_suggestions_title.localized()
        static let placeholderSuggestionsSubtitle = sharedStrings.search_placeholder_suggestions_subtitle.localized()

        static let placeholderErrorDescription = sharedStrings.search_placeholder_error_description.localized()
    }

    // MARK: - Questionnaire Onboarding -

    enum QuestionnaireOnboarding {
        static let textInputPlaceholder = sharedStrings.questionnaire_onboarding_text_input_placeholder.localized()

        static let sendButtot = sharedStrings.questionnaire_onboarding_send_button_text.localized()
        static let skipButton = sharedStrings.questionnaire_onboarding_skip_button_text.localized()
    }
}
