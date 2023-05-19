package org.hyperskill.app.step.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

@Serializable
sealed interface StepRoute {
    val analyticRoute: HyperskillAnalyticRoute
    val stepContext: StepContext
    val stepId: Long

    @Serializable
    data class Learn(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Step(stepId)

        @Transient
        override val stepContext: StepContext =
            StepContext.DEFAULT
    }

    @Serializable
    data class LearnDaily(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Learn.Daily(stepId)

        @Transient
        override val stepContext: StepContext =
            StepContext.DEFAULT
    }

    @Serializable
    data class Repeat(override val stepId: Long) : StepRoute {
        @Transient
        override val analyticRoute: HyperskillAnalyticRoute =
            HyperskillAnalyticRoute.Repeat.Step(stepId)

        @Transient
        override val stepContext: StepContext =
            StepContext.REPETITION
    }

    @Serializable
    data class StageImplement(
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
    }
}

internal fun StepRoute.copy(stepId: Long): StepRoute =
    when (this) {
        is StepRoute.Learn -> StepRoute.Learn(stepId)
        is StepRoute.LearnDaily -> StepRoute.LearnDaily(stepId)
        is StepRoute.Repeat -> StepRoute.Repeat(stepId)
        is StepRoute.StageImplement -> StepRoute.StageImplement(stepId, projectId, stageId)
    }