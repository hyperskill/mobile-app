import Foundation

enum Images {
    // MARK: - Common -

    enum Common {
        static let hyperskillLogo = "hyperskill-logo"
    }

    // MARK: - TabBar -

    enum TabBar {
        static let home = "tab-bar-home"
        static let homeFilled = "tab-bar-home-filled"

        static let track = "tab-bar-track"
        static let trackFilled = "tab-bar-track-filled"

        static let profile = "tab-bar-profile"
        static let profileFilled = "tab-bar-profile-filled"
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
    }

    // MARK: - Track -

    enum Track {
        static let projectGraduate = "track-project-graduate"
        static let hammer = "track-hammer"

        enum About {
            static let rating = "track-about-stat-item-star"
            static let project = "track-about-stat-item-project"
            static let topic = "track-about-stat-item-topic"
        }
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
    }

    // MARK: - Onboarding -

    enum Onboarding {
        static let problemOfDayCard = "onboarding-problem-of-day-card"
    }

    // MARK: - TopicsRepetitions -

    enum TopicsRepetitions {
        static let bookImage = "topics-repetitions-book-image"
    }
}
