package org.hyperskill.app.analytic.domain.model.hyperskill

sealed class HyperskillAnalyticRoute {
    abstract val path: String

    class Onboarding : HyperskillAnalyticRoute() {
        override val path: String = "/onboarding"
    }

    open class Login : HyperskillAnalyticRoute() {
        override val path: String = "/login"

        class Password : Login() {
            override val path: String =
                "${super.path}/password"
        }
    }

    class Register : HyperskillAnalyticRoute() {
        override val path: String = "/register"
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

    class Track : HyperskillAnalyticRoute() {
        override val path: String = "/track"
    }

    open class Profile : HyperskillAnalyticRoute() {
        override val path: String = "/profile"

        class Settings : Profile() {
            override val path: String =
                "${super.path}/settings"
        }
    }

    class Debug : HyperskillAnalyticRoute() {
        override val path: String = "/debug"
    }

    class StudyPlan : HyperskillAnalyticRoute() {
        override val path: String =
            "/study-plan"
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
}