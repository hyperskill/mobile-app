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
    sealed interface Learn : StepRoute {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Learn.Step(stepId)

        override val stepContext: StepContext
            get() = StepContext.DEFAULT

        @Serializable
        data class Step(override val stepId: Long) : Learn

        @Serializable
        data class TheoryOpenedFromPractice(override val stepId: Long) : Learn
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
    sealed interface Repeat : StepRoute {
        override val stepContext: StepContext
            get() = StepContext.REPETITION

        @Serializable
        data class Practice(override val stepId: Long) : Repeat {
            override val analyticRoute: HyperskillAnalyticRoute
                get() = HyperskillAnalyticRoute.Repeat.Step(stepId)
        }

        @Serializable
        data class Theory(override val stepId: Long) : Repeat {
            override val analyticRoute: HyperskillAnalyticRoute
                get() = HyperskillAnalyticRoute.Repeat.Step.Theory(stepId)
        }
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