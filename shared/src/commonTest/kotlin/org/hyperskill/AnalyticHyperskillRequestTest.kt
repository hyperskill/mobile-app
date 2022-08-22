package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_ACTION
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_CLIENT_TIME
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_CONTEXT
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_PART
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_PLATFORM
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_ROUTE
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_TARGET
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent.Companion.PARAM_USER
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.analytic.remote.model.AnalyticHyperskillRequest

class AnalyticHyperskillRequestTest {
    companion object {
        private const val TEST_USER_ID: Long = 18396
        private const val TEST_PLATFORM = "android"
        private const val TEST_CLIENT_TIME = "2017-07-13T16:59:00Z"
    }

    class TestHyperskillAnalyticEvent(
        private val userId: Long,
        route: HyperskillAnalyticRoute,
        action: HyperskillAnalyticAction,
        part: HyperskillAnalyticPart?,
        target: HyperskillAnalyticTarget?
    ) : HyperskillAnalyticEvent(route, action, part, target) {
        override val params: Map<String, Any>
            get() = super.params +
                mapOf(
                    PARAM_USER to userId,
                    PARAM_CONTEXT to mapOf(PARAM_PLATFORM to TEST_PLATFORM),
                    PARAM_CLIENT_TIME to TEST_CLIENT_TIME
                )
    }

    @Test
    fun requestSerializationTest() {
        val events = listOf(
            TestHyperskillAnalyticEvent(
                userId = TEST_USER_ID,
                route = HyperskillAnalyticRoute.Home(),
                action = HyperskillAnalyticAction.VIEW,
                part = HyperskillAnalyticPart.MAIN,
                target = HyperskillAnalyticTarget.SEND
            ),
            TestHyperskillAnalyticEvent(
                userId = TEST_USER_ID,
                route = HyperskillAnalyticRoute.Home(),
                action = HyperskillAnalyticAction.CLICK,
                part = null,
                target = null
            )
        )
        val request = AnalyticHyperskillRequest(events)
        val serializedValue = request.toJsonElement()

        val expected = buildJsonArray {
            addJsonObject {
                put(PARAM_CLIENT_TIME, TEST_CLIENT_TIME)
                put(PARAM_ROUTE, "/home")
                put(PARAM_ACTION, "view")
                put(PARAM_PART, "main")
                put(PARAM_TARGET, "send")
                put(PARAM_USER, TEST_USER_ID)
                putJsonObject(PARAM_CONTEXT) {
                    put(PARAM_PLATFORM, TEST_PLATFORM)
                }
            }
            addJsonObject {
                put(PARAM_CLIENT_TIME, TEST_CLIENT_TIME)
                put(PARAM_ROUTE, "/home")
                put(PARAM_ACTION, "click")
                put(PARAM_USER, TEST_USER_ID)
                putJsonObject(PARAM_CONTEXT) {
                    put(PARAM_PLATFORM, TEST_PLATFORM)
                }
            }
        }
        assertEquals(expected, serializedValue)
    }
}