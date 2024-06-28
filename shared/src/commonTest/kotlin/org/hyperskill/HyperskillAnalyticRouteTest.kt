package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.step.domain.model.StepRoute

class HyperskillAnalyticRouteTest {
    @Test
    fun projectStageImplementationTest() {
        val analyticRoute = HyperskillAnalyticRoute.Projects.Stages.Implement(projectId = 71, stageId = 390)
        val expected = "/projects/71/stages/390/implement"
        assertEquals(expected, analyticRoute.path)
    }

    @Test
    fun `Projects SelectProjectDetails analytic route is correct`() {
        val expected = "/projects/261?track=18"
        val actualAnalyticRoute = HyperskillAnalyticRoute.Projects.SelectProjectDetails(
            projectId = 261,
            trackId = 18
        )

        assertEquals(expected, actualAnalyticRoute.path)
    }

    @Test
    fun `Comment analytic route is correct`() {
        val stepRoutes = listOf(
            StepRoute.Learn.Step(stepId = 1, topicId = null),
            StepRoute.LearnDaily(stepId = 2, topicId = null),
            StepRoute.Repeat.Practice(stepId = 3, topicId = null)
        )
        val expected = listOf(
            "/learn/step/1#comment",
            "/learn/daily/2#comment",
            "/repeat/step/3#comment"
        )

        stepRoutes.forEachIndexed { index, stepRoute ->
            assertEquals(expected[index], HyperskillAnalyticRoute.Comment(stepRoute).path)
        }
    }
}