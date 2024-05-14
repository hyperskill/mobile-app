package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys
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
            HyperskillAnalyticKeys.PARAM_PLATFORM to TEST_PLATFORM,
            HyperskillAnalyticKeys.PARAM_SUBSCRIPTION_TYPE to SubscriptionType.FREEMIUM
        )
    ) {
        override val params: Map<String, Any>
            get() = super.params + mapOf(
                HyperskillAnalyticKeys.PARAM_USER to userId,
                HyperskillAnalyticKeys.PARAM_CLIENT_TIME to TEST_CLIENT_TIME
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
                put(HyperskillAnalyticKeys.PARAM_CLIENT_TIME, TEST_CLIENT_TIME)
                put(HyperskillAnalyticKeys.PARAM_ROUTE, "/home")
                put(HyperskillAnalyticKeys.PARAM_ACTION, "view")
                put(HyperskillAnalyticKeys.PARAM_PART, "main")
                put(HyperskillAnalyticKeys.PARAM_TARGET, "send")
                put(HyperskillAnalyticKeys.PARAM_USER, TEST_USER_ID)
                putJsonObject(HyperskillAnalyticKeys.PARAM_CONTEXT) {
                    put(HyperskillAnalyticKeys.PARAM_PLATFORM, TEST_PLATFORM)
                    put(HyperskillAnalyticKeys.PARAM_SUBSCRIPTION_TYPE, SubscriptionType.FREEMIUM.name)
                }
            }
            addJsonObject {
                put(HyperskillAnalyticKeys.PARAM_CLIENT_TIME, TEST_CLIENT_TIME)
                put(HyperskillAnalyticKeys.PARAM_ROUTE, "/home")
                put(HyperskillAnalyticKeys.PARAM_ACTION, "click")
                put(HyperskillAnalyticKeys.PARAM_USER, TEST_USER_ID)
                putJsonObject(HyperskillAnalyticKeys.PARAM_CONTEXT) {
                    put(HyperskillAnalyticKeys.PARAM_PLATFORM, TEST_PLATFORM)
                    put(HyperskillAnalyticKeys.PARAM_SUBSCRIPTION_TYPE, SubscriptionType.FREEMIUM.name)
                }
            }
        }
        assertEquals(expected, serializedValue)
    }
}