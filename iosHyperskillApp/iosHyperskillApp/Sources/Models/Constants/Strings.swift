import Foundation
import shared

// swiftlint:disable line_length
enum Strings {
    // MARK: Auth Social

    static let authSocialLogInTitle = SharedResources.strings.shared.auth_log_in_title.localized()
    static let authSocialJetBrainsAccount = SharedResources.strings.shared.auth_jetbrains_account_text.localized()
    static let authSocialGoogleAccount = SharedResources.strings.shared.auth_google_account_text.localized()
    static let authSocialGitHubAccount = SharedResources.strings.shared.auth_github_account_text.localized()
    static let authSocialAppleAccount = SharedResources.strings.shared.auth_apple_account_text.localized()
    static let authSocialEmailText = SharedResources.strings.shared.auth_email_text.localized()

    // MARK: Auth Email

    static let authEmailSocialText = SharedResources.strings.shared.auth_credentials_social_text.localized()
    static let authEmailResetPassword = SharedResources.strings.shared.auth_credentials_reset_password_text.localized()
    static let authEmailLogIn = SharedResources.strings.shared.auth_credentials_log_in_text.localized()
    static let authEmailPlaceholder = SharedResources.strings.shared.auth_credentials_email_placeholder.localized()
    static let authEmailPasswordPlaceholder = SharedResources.strings.shared.auth_credentials_password_placeholder.localized()
    static let authEmailLoginError = SharedResources.strings.shared.auth_credentials_error_text.localized()

    // MARK: Step

    static let stepStartPracticing = SharedResources.strings.shared.step_start_practicing_text.localized()

    // MARK: Choice Quiz

    static let choiceQuizCorrectStatusText = SharedResources.strings.shared.choice_quiz_correct_status_text.localized()
    static let choiceQuizWrongStatusText = SharedResources.strings.shared.choice_quiz_wrong_status_text.localized()
    static let choiceQuizCorrectFeedbackText = SharedResources.strings.shared.choice_quiz_correct_feedback_text.localized()
    static let choiceQuizWrongFeedbackText = SharedResources.strings.shared.choice_quiz_wrong_feedback_text.localized()
    static let choiceQuizStatText = SharedResources.strings.shared.choice_quiz_stat_text.localized()
    static let choiceQuizSingleOptionTaskText = SharedResources.strings.shared.choice_quiz_single_option_task_text.localized()
    static let choiceQuizMultipleOptionTaskText = SharedResources.strings.shared.choice_quiz_multiple_option_task_text.localized()
    static let choiceQuizFeedbackTitle = SharedResources.strings.shared.choice_quiz_feedback_title.localized()
    static let choiceQuizHintButtonText = SharedResources.strings.shared.choice_quiz_hint_button_text.localized()
    static let choiceQuizContinueButtonText = SharedResources.strings.shared.choice_quiz_continue_button_text.localized()
    static let choiceQuizRetryButtonText = SharedResources.strings.shared.choice_quiz_retry_button_text.localized()
    static let choiceQuizSendButtonText = SharedResources.strings.shared.choice_quiz_send_button_text.localized()
    static let choiceQuizDiscussionsButtonText = SharedResources.strings.shared.choice_quiz_discussions_button_text.localized()
}
