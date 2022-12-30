package org.hyperskill.app.step.domain.model

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

sealed class StepRoute(val stepId: Long) {
    abstract val analyticRoute: HyperskillAnalyticRoute

    class Learn(stepId: Long) : StepRoute(stepId) {
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Step(super.stepId)
    }

    class LearnDaily(stepId: Long) : StepRoute(stepId) {
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Daily(super.stepId)
    }

    class Repeat(stepId: Long) : StepRoute(stepId) {
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Repeat.Step(super.stepId)
    }
}