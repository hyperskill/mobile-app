package org.hyperskill.app.step.domain.model

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

sealed class StepRoute(val stepId: Long) {
    abstract val analyticRoute: HyperskillAnalyticRoute

    class LearnStepRoute(stepId: Long) : StepRoute(stepId) {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Learn.Step(super.stepId)
    }

    class LearnDailyStepRoute(stepId: Long) : StepRoute(stepId) {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Learn.Daily(super.stepId)
    }

    class RepeatStepRoute(stepId: Long) : StepRoute(stepId) {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Repeat.Step(super.stepId)
    }
}