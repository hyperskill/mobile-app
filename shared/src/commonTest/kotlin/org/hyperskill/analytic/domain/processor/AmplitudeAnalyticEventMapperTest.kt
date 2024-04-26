package org.hyperskill.analytic.domain.processor

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent
import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.analytic.domain.processor.AmplitudeAnalyticEventMapper

class AmplitudeAnalyticEventMapperTest {
    class TestViewHyperskillAnalyticEvent(
        route: HyperskillAnalyticRoute = HyperskillAnalyticRoute.Debug()
    ) : HyperskillAnalyticEvent(
        route = route,
        action = HyperskillAnalyticAction.VIEW
    )

    class TestClickHyperskillAnalyticEvent(
        flag: Boolean
    ) : HyperskillAnalyticEvent(
        route = HyperskillAnalyticRoute.Debug(),
        action = HyperskillAnalyticAction.CLICK,
        part = HyperskillAnalyticPart.MAIN,
        target = HyperskillAnalyticTarget.DEBUG,
        context = mapOf(PARAM_FLAG to flag)
    ) {
        companion object {
            const val PARAM_FLAG = "flag"
        }
    }

    @Test
    fun `map should return AmplitudeAnalyticEvent when HyperskillAnalyticEvent is provided`() {
        val actual = AmplitudeAnalyticEventMapper.map(TestViewHyperskillAnalyticEvent())
        assertNotNull(actual)
    }

    @Test
    fun `map should return same AmplitudeAnalyticEvent when AmplitudeAnalyticEvent is provided`() {
        val amplitudeEvent = AmplitudeAnalyticEvent(
            name = "testName",
            params = mapOf("key" to "value")
        )

        val actual = AmplitudeAnalyticEventMapper.map(amplitudeEvent)

        assertEquals(amplitudeEvent, actual)
    }

    @Test
    fun `map should return null when neither HyperskillAnalyticEvent nor AmplitudeAnalyticEvent is provided`() {
        val otherEvent = AppsFlyerAnalyticEvent(name = "testName")

        val result = AmplitudeAnalyticEventMapper.map(otherEvent)

        assertNull(result)
    }

    @Test
    fun `map should include path in name when HyperskillAnalyticEvent with VIEW action is provided`() {
        val hyperskillEvent = TestViewHyperskillAnalyticEvent()

        val actual = AmplitudeAnalyticEventMapper.map(hyperskillEvent)

        assertTrue(actual!!.name.contains(hyperskillEvent.route.path))
    }

    @Test
    fun `map should correctly transform TestClickHyperskillAnalyticEvent to AmplitudeAnalyticEvent`() {
        val testClickEvent = TestClickHyperskillAnalyticEvent(flag = true)

        val actual = AmplitudeAnalyticEventMapper.map(testClickEvent)!!

        assertEquals("click main debug", actual.name)
        assertEquals(
            mapOf(
                HyperskillAnalyticKeys.PARAM_ROUTE to "/debug",
                TestClickHyperskillAnalyticEvent.PARAM_FLAG to true
            ),
            actual.params
        )
    }

    @Test
    fun `map should correctly set name for view step screens`() {
        val events = listOf(
            TestViewHyperskillAnalyticEvent(
                route = HyperskillAnalyticRoute.Learn.Step(1L)
            ),
            TestViewHyperskillAnalyticEvent(
                route = HyperskillAnalyticRoute.Learn.Daily(1L)
            ),
            TestViewHyperskillAnalyticEvent(
                route = HyperskillAnalyticRoute.Projects.Stages.Implement(
                    projectId = 1L,
                    stageId = 2L
                )
            ),
            TestViewHyperskillAnalyticEvent(
                route = HyperskillAnalyticRoute.Repeat.Step(1L)
            ),
            TestViewHyperskillAnalyticEvent(
                route = HyperskillAnalyticRoute.Repeat.Step.Theory(1L)
            )
        )

        events.forEach { event ->
            val result = AmplitudeAnalyticEventMapper.map(event)!!
            assertEquals(
                "view step",
                result.name,
                "Event name should be 'view step' for VIEW action in step screens"
            )
        }
    }
}