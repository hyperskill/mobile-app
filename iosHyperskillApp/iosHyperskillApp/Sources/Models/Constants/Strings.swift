import Foundation
import shared

enum Strings {
    private static let sharedStrings = SharedResources.strings.shared

    // MARK: - AuthSocial -

    enum AuthSocial {
        static let logInTitle = sharedStrings.auth_log_in_title.localized()
        static let jetBrainsAccount = sharedStrings.auth_jetbrains_account_text.localized()
        static let googleAccount = sharedStrings.auth_google_account_text.localized()
        static let gitHubAccount = sharedStrings.auth_github_account_text.localized()
        static let appleAccount = sharedStrings.auth_apple_account_text.localized()
        static let emailText = sharedStrings.auth_email_text.localized()
    }

    // MARK: - AuthCredentials -

    enum AuthCredentials {
        static let socialText = sharedStrings.auth_credentials_social_text.localized()
        static let resetPassword = sharedStrings.auth_credentials_reset_password_text.localized()
        static let logIn = sharedStrings.auth_credentials_log_in_text.localized()
        static let emailPlaceholder = sharedStrings.auth_credentials_email_placeholder.localized()
        static let passwordPlaceholder = sharedStrings.auth_credentials_password_placeholder.localized()
    }

    // MARK: - Step -

    enum Step {
        static let startPracticing = sharedStrings.step_start_practicing_text.localized()
    }

    // MARK: - StepQuiz -

    enum StepQuiz {
        static let quizStatusCorrect = sharedStrings.step_quiz_status_correct_text.localized()
        static let quizStatusWrong = sharedStrings.step_quiz_status_wrong_text.localized()
        static let feedbackTitle = sharedStrings.step_quiz_feedback_title.localized()
        static let hintButton = sharedStrings.step_quiz_hint_button_text.localized()
        static let continueButton = sharedStrings.step_quiz_continue_button_text.localized()
        static let retryButton = sharedStrings.step_quiz_retry_button_text.localized()
        static let sendButton = sharedStrings.step_quiz_send_button_text.localized()
        static let checkingButton = sharedStrings.step_quiz_checking_button_text.localized()
        static let discussionsButton = sharedStrings.step_quiz_discussions_button_text.localized()
    }

    // MARK: - StepQuizChoice -

    enum StepQuizChoice {
        static let singleChoiceTitle = sharedStrings.step_quiz_choice_single_choice_title.localized()
        static let multipleChoiceTitle = sharedStrings.step_quiz_choice_multiple_choice_title.localized()
    }
}
