package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.hyperskill.app.analytic.domain.model.AnalyticKeys
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.analytic.remote.model.AnalyticHyperskillRequest
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

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
    ) : HyperskillAnalyticEvent(
        route = route,
        action = action,
        part = part,
        target = target,
        context = mapOf(
            AnalyticKeys.PARAM_PLATFORM to TEST_PLATFORM,
            AnalyticKeys.PARAM_SUBSCRIPTION_TYPE to SubscriptionType.FREEMIUM
        )
    ) {
        override val params: Map<String, Any>
            get() = super.params + mapOf(
                AnalyticKeys.PARAM_USER to userId,
                AnalyticKeys.PARAM_CLIENT_TIME to TEST_CLIENT_TIME
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
                put(AnalyticKeys.PARAM_CLIENT_TIME, TEST_CLIENT_TIME)
                put(AnalyticKeys.PARAM_ROUTE, "/home")
                put(AnalyticKeys.PARAM_ACTION, "view")
                put(AnalyticKeys.PARAM_PART, "main")
                put(AnalyticKeys.PARAM_TARGET, "send")
                put(AnalyticKeys.PARAM_USER, TEST_USER_ID)
                putJsonObject(AnalyticKeys.PARAM_CONTEXT) {
                    put(AnalyticKeys.PARAM_PLATFORM, TEST_PLATFORM)
                    put(AnalyticKeys.PARAM_SUBSCRIPTION_TYPE, SubscriptionType.FREEMIUM.name)
                }
            }
            addJsonObject {
                put(AnalyticKeys.PARAM_CLIENT_TIME, TEST_CLIENT_TIME)
                put(AnalyticKeys.PARAM_ROUTE, "/home")
                put(AnalyticKeys.PARAM_ACTION, "click")
                put(AnalyticKeys.PARAM_USER, TEST_USER_ID)
                putJsonObject(AnalyticKeys.PARAM_CONTEXT) {
                    put(AnalyticKeys.PARAM_PLATFORM, TEST_PLATFORM)
                    put(AnalyticKeys.PARAM_SUBSCRIPTION_TYPE, SubscriptionType.FREEMIUM.name)
                }
            }
        }
        assertEquals(expected, serializedValue)
    }
}