package org.hyperskill.subscriptions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Instant
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionStatus
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType

class SubscriptionSerializationTest {
    companion object {
        private const val TEST_JSON = """
            {
               "type": "freemium",
               "steps_limit_total": 10,
               "steps_limit_left": 9,
               "steps_limit_reset_time": "2022-11-16T12:19:14.782644Z",
               "valid_till": "2099-01-01T01:00:00Z"
            }
        """

        private val EXPECTED_SUBSCRIPTION: Subscription =
            Subscription(
                type = SubscriptionType.FREEMIUM,
                status = SubscriptionStatus.ACTIVE,
                stepsLimitTotal = 10,
                stepsLimitLeft = 9,
                stepsLimitResetTime = Instant.parse("2022-11-16T12:19:14.782644Z"),
                validTill = Instant.parse("2099-01-01T01:00:00Z")
            )
    }

    @Test
    fun `Serialized subscription should be deserialized normally`() {
        val json = NetworkModule.provideJson()
        val actualModel = json.decodeFromString(Subscription.serializer(), TEST_JSON)
        assertEquals(EXPECTED_SUBSCRIPTION, actualModel)
    }
}