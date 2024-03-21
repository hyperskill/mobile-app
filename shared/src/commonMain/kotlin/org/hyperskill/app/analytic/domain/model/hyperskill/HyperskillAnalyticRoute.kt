package org.hyperskill.app.analytic.domain.model.hyperskill

sealed class HyperskillAnalyticRoute {
    abstract val path: String

    open class Onboarding : HyperskillAnalyticRoute() {
        override val path: String = "/onboarding"

        object Notifications : Onboarding() {
            override val path: String
                get() = "${super.path}/notifications"
        }

        object FirstProblem : Onboarding() {
            override val path: String
                get() = "${super.path}/first-problem"
        }

        object UsersQuestionnaire : Onboarding() {
            override val path: String
                get() = "${super.path}/questionnaire"
        }
    }

    open class Login : HyperskillAnalyticRoute() {
        override val path: String = "/login"

        class Password : Login() {
            override val path: String =
                "${super.path}/password"
        }
    }

    sealed class Learn : HyperskillAnalyticRoute() {
        override val path: String = "/learn"

        class Daily(stepId: Long) : Learn() {
            override val path: String =
                "${super.path}/daily/$stepId"
        }

        class Step(stepId: Long) : Learn() {
            override val path: String =
                "${super.path}/step/$stepId"
        }
    }

    sealed class Projects(projectId: Long) : HyperskillAnalyticRoute() {
        override val path: String = "/projects/$projectId"

        class SelectProjectDetails(projectId: Long, trackId: Long) : Projects(projectId) {
            override val path: String =
                "${super.path}?track=$trackId"
        }

        sealed class Stages(projectId: Long, stageId: Long) : Projects(projectId) {
            override val path: String =
                "${super.path}/stages/$stageId"

            class Implement(projectId: Long, stageId: Long) : Stages(projectId, stageId) {
                override val path: String =
                    "${super.path}/implement"
            }
        }
    }

    class Home : HyperskillAnalyticRoute() {
        override val path: String = "/home"
    }

    open class Repeat : HyperskillAnalyticRoute() {
        override val path: String = "/repeat"

        open class Step(stepId: Long) : Repeat() {
            override val path: String =
                "${super.path}/step/$stepId"

            class Theory(stepId: Long) : Step(stepId) {
                override val path: String =
                    "${super.path}?theory"
            }
        }
    }

    open class Profile : HyperskillAnalyticRoute() {
        override val path: String = "/profile"

        open class Settings : Profile() {
            override val path: String =
                "${super.path}/settings"

            object ManageSubscription : Settings() {
                override val path: String
                    get() = "${super.path}/manage-subscription"
            }
        }
    }

    class Debug : HyperskillAnalyticRoute() {
        override val path: String = "/debug"
    }

    open class StudyPlan : HyperskillAnalyticRoute() {
        override val path: String = "/study-plan"

        class UsersQuestionnaireWidget : StudyPlan() {
            override val path: String
                get() = "${super.path}/users-questionnaire-widget"
        }
    }

    class Leaderboard : HyperskillAnalyticRoute() {
        override val path: String = "/leaderboard"
    }

    open class Tracks : HyperskillAnalyticRoute() {
        override val path: String = "/tracks"

        class Details(trackId: Long) : Tracks() {
            override val path: String =
                "${super.path}/$trackId"
        }

        class Projects(trackId: Long) : Tracks() {
            override val path: String =
                "${super.path}/$trackId/projects"
        }
    }

    class Progress : HyperskillAnalyticRoute() {
        override val path: String = "/progress"
    }

    class Search : HyperskillAnalyticRoute() {
        override val path: String = "/search"
    }

    /**
     * Represents a special route when we do not know where the events is occurred (ALTAPPS-1086).
     */
    object None : HyperskillAnalyticRoute() {
        override val path: String = "None"
    }

    /**
     * Springboard, or Home Screen is the standard application that manages the home screen of Apple devices.
     */
    class IosSpringBoard : HyperskillAnalyticRoute() {
        override val path: String = "SpringBoard"
    }

    object Paywall : HyperskillAnalyticRoute() {
        override val path: String
            get() = "/paywall"
    }

    /**
     * Represents a special route that is used to track the first time the app is launched (ALTAPPS-1139).
     */
    internal class AppLaunchFirstTime : HyperskillAnalyticRoute() {
        override val path: String = "app-launch-first-time"
    }
}