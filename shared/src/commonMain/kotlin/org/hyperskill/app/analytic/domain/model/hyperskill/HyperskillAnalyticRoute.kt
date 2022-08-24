package org.hyperskill.app.analytic.domain.model.hyperskill

sealed class HyperskillAnalyticRoute {
    abstract val path: String

    class Onboarding : HyperskillAnalyticRoute() {
        override val path: String = "/onboarding"
    }

    class Home : HyperskillAnalyticRoute() {
        override val path: String = "/home"
    }

    open class Learn : HyperskillAnalyticRoute() {
        override val path: String = "/learn"

        class Daily(stepId: Long) : Learn() {
            override val path: String = "${super.path}/daily/$stepId"
        }

        class Step(stepId: Long) : Learn() {
            override val path: String = "${super.path}/step/$stepId"
        }
    }
}