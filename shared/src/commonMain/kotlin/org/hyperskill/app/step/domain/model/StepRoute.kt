package org.hyperskill.app.step.domain.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

@Serializable
sealed interface StepRoute {
    val analyticRoute: HyperskillAnalyticRoute
    val stepContext: StepContext
    val stepId: Long
    val topicId: Long?

    @Serializable
    sealed interface Learn : StepRoute {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Learn.Step(stepId)

        override val stepContext: StepContext
            get() = StepContext.DEFAULT

        @Serializable
        data class Step(
            override val stepId: Long,
            override val topicId: Long?
        ) : Learn

        @Serializable
        data class TheoryOpenedFromPractice(
            override val stepId: Long,
            override val topicId: Long?
        ) : Learn

        @Serializable
        data class TheoryOpenedFromSearch(
            override val stepId: Long,
            override val topicId: Long? = null
        ) : Learn
    }

    @Serializable
    data class LearnDaily(
        override val stepId: Long,
        override val topicId: Long? = null
    ) : StepRoute {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Learn.Daily(stepId)

        override val stepContext: StepContext
            get() = StepContext.DEFAULT
    }

    @Serializable
    sealed interface Repeat : StepRoute {
        override val stepContext: StepContext
            get() = StepContext.REPETITION

        @Serializable
        data class Practice(
            override val stepId: Long,
            override val topicId: Long? = null
        ) : Repeat {
            override val analyticRoute: HyperskillAnalyticRoute
                get() = HyperskillAnalyticRoute.Repeat.Step(stepId)
        }

        @Serializable
        data class Theory(
            override val stepId: Long,
            override val topicId: Long? = null
        ) : Repeat {
            override val analyticRoute: HyperskillAnalyticRoute
                get() = HyperskillAnalyticRoute.Repeat.Step.Theory(stepId)
        }
    }

    @Serializable
    data class StageImplement(
        override val stepId: Long,
        val projectId: Long,
        val stageId: Long,
        override val topicId: Long? = null
    ) : StepRoute {
        override val analyticRoute: HyperskillAnalyticRoute
            get() = HyperskillAnalyticRoute.Projects.Stages.Implement(projectId, stageId)

        override val stepContext: StepContext
            get() = StepContext.DEFAULT
    }
}