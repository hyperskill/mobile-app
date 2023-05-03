package org.hyperskill.app.step.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

@Serializable
sealed interface StepRoute {
    val analyticRoute: HyperskillAnalyticRoute
    val stepContext: StepContext
    val stepId: Long

    /**
     * A boolean flag that indicates whether a user has any limitations in the freemium mode.
     * For example a user has not limitations when solving problem of the day and when repeating a step.
     */
    val isFreemiumUnlimited: Boolean

    @Serializable
    class Learn(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Step(stepId)

        @Transient
        override val stepContext: StepContext =
            StepContext.DEFAULT

        override val isFreemiumUnlimited: Boolean = false
    }

    @Serializable
    class LearnDaily(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Daily(stepId)

        @Transient
        override val stepContext: StepContext =
            StepContext.DEFAULT

        override val isFreemiumUnlimited: Boolean = true
    }

    @Serializable
    class Repeat(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Repeat.Step(stepId)

        @Transient
        override val stepContext: StepContext =
            StepContext.REPETITION

        override val isFreemiumUnlimited: Boolean = true
    }

    @Serializable
    class StageImplement(
        override val stepId: Long,
        val projectId: Long,
        val stageId: Long,
    ) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Projects.Stages.Implement(projectId, stageId)

        @Transient
        override val stepContext: StepContext =
            StepContext.DEFAULT

        override val isFreemiumUnlimited: Boolean = false
    }
}