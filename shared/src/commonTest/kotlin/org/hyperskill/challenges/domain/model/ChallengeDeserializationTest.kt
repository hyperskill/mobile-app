package org.hyperskill.challenges.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Instant
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.domain.model.ChallengeTargetType
import org.hyperskill.app.network.injection.NetworkModule

class ChallengeDeserializationTest {

    companion object {
        private const val ID: Long = 6
        private const val TITLE = "QA  ☾⋆"
        private const val DESCRIPTION = "The Challenge! Ho-ho-ho!🎅\nHurry up and get yor prise!"
        private val TARGET_TYPE = ChallengeTargetType.STEP.value
        private const val START = "2024-05-20T04:00:00Z"
        private const val END = "2024-05-20T04:00:00Z"
        private const val NEXT_INTERVAL_TIME = "2024-05-20T04:00:00Z"
        private const val INTERVALS_COUNT = 1
        private const val STATUS = "not completed"
        private val REWARD_LINK: String? = null
        private const val PROGRESS = false
        private val CURRENT_INTERVAL: Int? = null

        private val TEST_JSON_STRING = """
{
    "id": $ID,
    "title": "$TITLE",
    "description": "$DESCRIPTION",
    "target_type": "$TARGET_TYPE",
    "start": "$START",
    "end": "$END",
    "intervals_count": $INTERVALS_COUNT,
    "next_interval_time": "$NEXT_INTERVAL_TIME",
    "status": "$STATUS",
    "reward_link": $REWARD_LINK,
    "progress":
    [
        $PROGRESS
    ],
    "current_interval": $CURRENT_INTERVAL
}
        """.trimIndent()
    }

    @Test
    fun `Test Challenge deserialization`() {
        val json = NetworkModule.provideJson()
        val expected = Challenge(
            id = ID,
            title = TITLE,
            description = DESCRIPTION,
            targetTypeValue = TARGET_TYPE,
            start = Instant.parse(START),
            end = Instant.parse(END),
            intervalsCount = INTERVALS_COUNT,
            statusValue = STATUS,
            rewardLink = REWARD_LINK,
            progress = listOf(PROGRESS),
            currentInterval = CURRENT_INTERVAL,
            nextIntervalTime = Instant.parse(NEXT_INTERVAL_TIME)
        )
        val decodedObject = json.decodeFromString(Challenge.serializer(), TEST_JSON_STRING)
        assertEquals(expected, decodedObject)
    }
}