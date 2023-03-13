package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

class HyperskillAnalyticRouteTest {
    @Test
    fun projectStageImplementationTest() {
        val analyticRoute = HyperskillAnalyticRoute.Projects.Stages.Implement(projectId = 71, stageId = 390)
        val expected = "/projects/71/stages/390/implement"
        assertEquals(expected, analyticRoute.path)
    }
}