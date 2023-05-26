import Foundation

enum Images {
    // MARK: - Common -

    enum Common {
        static let hyperskillLogo = "hyperskill-logo"

        static let trophy = "trophy"
    }

    // MARK: - TabBar -

    enum TabBar {
        static let home = "tab-bar-home"
        static let homeFilled = "tab-bar-home-filled"

        static let studyPlan = "tab-bar-study-plan"
        static let studyPlanFilled = "tab-bar-study-plan-filled"

        static let profile = "tab-bar-profile"
        static let profileFilled = "tab-bar-profile-filled"
    }

    // MARK: - NavigationBar -

    enum NavigationBar {
        static let streakCompleted = "navigation-bar-streak-completed"
        static let streakUncompleted = "navigation-bar-streak-uncompleted"
        static let gems = "navigation-bar-gems"
    }

    // MARK: - AuthSocial -

    enum AuthSocial {
        static let apple = "auth-social-apple-logo"
        static let github = "auth-social-github-logo"
        static let google = "auth-social-google-logo"
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

        enum Hints {
            static let helpfulReaction = "step_quiz_hints_helpful_reaction"
            static let unhelpfulReaction = "step_quiz_hints_unhelpful_reaction"
        }

        enum ProblemOfDaySolvedModal {
            static let book = "problem-of-day-solved-modal-book"
            static let gemsBadge = "problem-of-day-solved-modal-gems-badge"
        }

        enum ProblemsLimitReachedModal {
            static let icon = "problems-limit-reached-modal-icon"
        }
    }

    // MARK: - StageImplement -

    enum StageImplement {
        enum UnsupportedModal {
            static let icon = "stage-implement-unsupported-modal-icon"
        }
    }

    // MARK: - Track -

    enum Track {
        static let projectGraduate = "track-project-graduate"
        static let hammer = "track-hammer"

        static let track = "track"
        static let trackFilled = "track-filled"

        enum TopicsToDiscoverNext {
            static let skippedTopic = "track-topics-to-discover-next-skipped-topic"
        }

        enum About {
            static let rating = "track-about-stat-item-star"
            static let project = "track-about-stat-item-project"
            static let topic = "track-about-stat-item-topic"
        }
    }

    // MARK: - Placeholder -

    enum Placeholder {
        static let networkError = "placeholder-network-error"
        static let reload = "placeholder-reload"
    }

    // MARK: - Home -

    enum Home {
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

    // MARK: - Profile -

    enum Profile {
        enum About {
            enum Social {
                static let facebook = "profile-about-social-facebook"
                static let twitter = "profile-about-social-twitter"
                static let linkedIn = "profile-about-social-linkedin"
                static let reddit = "profile-about-social-reddit"
                static let github = "profile-about-social-github"
            }
        }

        enum Streak {
            enum Card {
                static let streakActive = "profile-streak-active"
                static let streakPassive = "profile-streak-passive"
                static let streakFrozen = "profile-streak-frozen"
                static let crown = "profile-streak-crown"
            }

            enum FreezeModal {
                static let diamond = "profile-streak-freeze-modal-diamond"
                static let snowflake = "profile-streak-freeze-modal-snowflake"
                static let gemsBadgeLocked = "profile-streak-freeze-modal-gems-badge-locked"
                static let snowflakeBadge = "profile-streak-freeze-modal-snowflake-badge"
            }
        }
    }

    // MARK: - Onboarding -

    enum Onboarding {
        static let problemOfDayCard = "onboarding-problem-of-day-card"
    }

    // MARK: - TopicsRepetitions -

    enum TopicsRepetitions {
        static let bookImage = "topics-repetitions-book-image"
    }

    // MARK: - ProjectSelectionList -

    enum ProjectSelectionList {
        static let projectGraduate = "project-selection-list-project-graduate"

        enum ProjectLevel {
            static let easy = "project-selection-list-project-level-easy"
            static let medium = "project-selection-list-project-level-medium"
            static let hard = "project-selection-list-project-level-hard"
            static let nightmare = "project-selection-list-project-level-nightmare"
        }
    }
}
