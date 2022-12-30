package org.hyperskill.app.step.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

@Serializable
sealed interface StepRoute {
    val analyticRoute: HyperskillAnalyticRoute
    val stepId: Long

    @Serializable
    class Learn(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Step(stepId)
    }

    @Serializable
    class LearnDaily(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Daily(stepId)
    }

    @Serializable
    class Repeat(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Repeat.Step(stepId)
    }
}