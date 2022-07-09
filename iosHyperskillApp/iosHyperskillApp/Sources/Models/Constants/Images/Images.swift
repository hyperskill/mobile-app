import Foundation

enum Images {
    // MARK: - TabBar -

    enum TabBar {
        static let home = "tab-bar-home"

        static let track = "tab-bar-track"

        static let profile = "tab-bar-profile"
    }

    // MARK: - AuthSocial -

    enum AuthSocial {
        static let apple = "auth-social-apple-logo"

        static let github = "auth-social-github-logo"

        static let google = "auth-social-google-logo"

        static let hyperskill = "auth-social-hyperskill-logo"

        static let jetbrains = "auth-social-jetbrains-logo"
    }

    // MARK: - Step -

    enum Step {
        static let clock = "step-time-to-complete"

        enum Rating {
            static let angry = "step-rating-angry"

            static let happy = "step-rating-happy"

            static let inLove = "step-rating-in-love"

            static let neutral = "step-rating-neutral"

            static let sad = "step-rating-sad"
        }
    }

    // MARK: - StepQuiz -

    enum StepQuiz {
        static let checkmark = "step-quiz-checkmark"

        static let discussions = "step-quiz-discussions"

        static let info = "step-quiz-info"

        static let lightning = "step-quiz-lightning"
    }

    // MARK: - Placeholder -

    enum Placeholder {
        static let networkError = "placeholder-network-error"
    }

    // MARK: - Home -

    enum Home {
        enum Streak {
            static let streakActive = "home-streak-active"
            static let streakPassive = "home-streak-passive"
            static let streakFrozen = "home-streak-frozen"
            static let crown = "home-streak-crown"
        }

        enum ProblemOfDay {
            static let calendar = "problem-of-day-calendar"
            static let done = "problem-of-day-done"
            static let gembox = "problem-of-day-gembox"
            static let hexogensCompleted = "problem-of-day-hexogens-completed"
            static let hexogensUncompleted = "problem-of-day-hexogens-uncompleted"
            static let arrowCompleted = "problem-of-day-arrow-completed"
            static let arrowUncompleted = "problem-of-day-arrow-uncompleted"
        }
    }
}
