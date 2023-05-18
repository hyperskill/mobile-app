package org.hyperskill.step

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.domain.model.copy

class StepRouteTest {
    @Test
    fun `StepRoute copy with new step id`() {
        val initialStepId = 1L
        val initialStepRoutes = listOf(
            StepRoute.Learn(initialStepId),
            StepRoute.LearnDaily(initialStepId),
            StepRoute.Repeat(initialStepId),
            StepRoute.StageImplement(initialStepId, 2L, 3L),
        )

        val expectedStepId = 2L
        val expectedStepRoutes = listOf(
            StepRoute.Learn(expectedStepId),
            StepRoute.LearnDaily(expectedStepId),
            StepRoute.Repeat(expectedStepId),
            StepRoute.StageImplement(expectedStepId, 2L, 3L),
        )

        initialStepRoutes.forEachIndexed { index, initialStepRoute ->
            val expectedStepRoute = expectedStepRoutes[index]
            val actualStepRoute = initialStepRoute.copy(expectedStepId)

            assertEquals(expectedStepRoute::class, actualStepRoute::class)
            assertEquals(expectedStepRoute.stepId, actualStepRoute.stepId)
            assertEquals(expectedStepRoute.analyticRoute.path, actualStepRoute.analyticRoute.path)
            assertEquals(expectedStepRoute.stepContext, actualStepRoute.stepContext)

            if (expectedStepRoute is StepRoute.StageImplement) {
                assertEquals(
                    expectedStepRoute.projectId,
                    (actualStepRoute as StepRoute.StageImplement).projectId
                )
                assertEquals(expectedStepRoute.stageId, actualStepRoute.stageId)
            }
        }
    }
}