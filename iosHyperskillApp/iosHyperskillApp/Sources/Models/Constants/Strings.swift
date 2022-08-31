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
    }

    // MARK: - TabBar -

    enum TabBar {
        static let home = sharedStrings.tab_bar_home_title.localized()
        static let track = sharedStrings.tab_bar_track_title.localized()
        static let profile = sharedStrings.tab_bar_profile_title.localized()
    }

    // MARK: - Auth -

    enum Auth {
        // MARK: Social

        enum Social {
            static let logInTitle = sharedStrings.auth_log_in_title.localized()
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
            static let title = sharedStrings.placeholder_register_text.localized()
            static let introText = sharedStrings.placeholder_instruction_text.localized()
            static let continueButton = sharedStrings.placeholder_continue_button_text_ios.localized()
            static let signInButton = Onboarding.signIn
            static let possibilityText = sharedStrings.placeholder_first_description_text.localized()
            static let callText = sharedStrings.placeholder_second_description_text.localized()
        }
    }

    // MARK: - Step -

    enum Step {
        static let startPracticing = sharedStrings.step_start_practicing_text.localized()
    }

    // MARK: - StepQuiz -

    enum StepQuiz {
        static let quizStatusCorrect = sharedStrings.step_quiz_status_correct_text.localized()
        static let quizStatusWrong = sharedStrings.step_quiz_status_wrong_text.localized()
        static let quizStatusEvaluation = sharedStrings.step_quiz_status_evaluation_text.localized()
        static let feedbackTitle = sharedStrings.step_quiz_feedback_title.localized()
        static let hintButton = sharedStrings.step_quiz_hint_button_text.localized()
        static let continueButton = sharedStrings.step_quiz_continue_button_text.localized()
        static let retryButton = sharedStrings.step_quiz_retry_button_text.localized()
        static let sendButton = sharedStrings.step_quiz_send_button_text.localized()
        static let checkingButton = sharedStrings.step_quiz_checking_button_text.localized()
        static let discussionsButton = sharedStrings.step_quiz_discussions_button_text.localized()
        static let unsupportedText = sharedStrings.step_quiz_unsupported_quiz_text.localized()
        static let afterDailyStepCompletedDialogTitle =
            sharedStrings.after_daily_step_completed_dialog_title.localized()
        static let afterDailyStepCompletedDialogText = sharedStrings.after_daily_step_completed_dialog_text.localized()
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
        static let resetCodeDialogTitle = sharedStrings.reset_code_dialog_title.localized()
        static let resetCodeDialogExplanation = sharedStrings.reset_code_dialog_explanation.localized()
        static let fullScreenDetailsTab = sharedStrings.step_quiz_code_full_screen_details_tab.localized()
        static let fullScreenCodeTab = sharedStrings.step_quiz_code_full_screen_code_tab.localized()
        static let emptyLang = sharedStrings.step_quiz_code_empty_lang.localized()
        static let reset = sharedStrings.step_quiz_code_reset.localized()
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
        static let helloLetsLearn = sharedStrings.home_hello_lets_learn_text.localized()
        static let keepPracticing = sharedStrings.home_keep_practicing_text.localized()
    }

    // MARK: - Track -

    enum Track {
        static let title = sharedStrings.track_title.localized()
        static let learningNow = sharedStrings.track_learning_now_text.localized()
        static let timeToComplete = sharedStrings.track_time_to_complete_text.localized()
        static let completedGraduateProject = sharedStrings.track_completed_graduate_project_text.localized()
        static let completedTopics = sharedStrings.track_completed_topics_text.localized()
        static let appliedCoreTopics = sharedStrings.track_applied_core_topics_text.localized()
        static let about = sharedStrings.track_about_text.localized()
        static let continueInWebButton = sharedStrings.track_continue_in_web_text.localized()
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
        static let helpCenter = sharedStrings.settings_help_center.localized()
        static let version = sharedStrings.settings_version.localized()
        static let rateApplication = sharedStrings.settings_rate_application.localized()
        static let logout = sharedStrings.settings_logout.localized()
        static let logoutDialogTitle = sharedStrings.settings_logout_dialog_title.localized()
        static let logoutDialogExplanation = sharedStrings.settings_logout_dialog_explanation.localized()
        static let deleteAccount = sharedStrings.settings_delete_account.localized()
        static let deleteAccountAlertTitle = sharedStrings.settings_account_deletion_dialog_title.localized()
        static let deleteAccountAlertMessage = sharedStrings.settings_account_deletion_dialog_explanation.localized()
        static let deleteAccountAlertDeleteButton =
            sharedStrings.settings_account_deletion_dialog_delete_button_text.localized()
        static let termsOfServiceURL = sharedStrings.settings_terms_of_service_url.localized()
        static let privacyPolicyURL = sharedStrings.settings_privacy_policy_url.localized()
        static let helpCenterURL = sharedStrings.settings_help_center_url.localized()
        static let accountDeletionURL = sharedStrings.settings_account_deletion_url.localized()

        enum Theme {
            static let title = sharedStrings.settings_theme.localized()

            static let light = sharedStrings.settings_theme_light.localized()
            static let dark = sharedStrings.settings_theme_dark.localized()
            static let system = sharedStrings.settings_theme_system.localized()
        }
    }

    // MARK: - Streak -

    enum Streak {
        static let solvingProblemText = sharedStrings.streak_solving_problem_text.localized()
        static let keepSolvingProblemsText = sharedStrings.streak_keep_solving_problems_text.localized()
        static let previousFiveDaysText = sharedStrings.streak_previous_five_days_text.localized()
        static let todayText = sharedStrings.streak_today_text.localized()
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

    // MARK: - Onboarding -

    enum Onboarding {
        static let title = sharedStrings.onboarding_title.localized()
        static let text = sharedStrings.onboarding_text.localized()
        static let signIn = sharedStrings.onboarding_sign_in.localized()
        static let signUp = sharedStrings.onboarding_sign_up.localized()
    }
}
