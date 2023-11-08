package org.hyperskill.challenges

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.LocalDate
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.network.injection.NetworkModule

class ChallengeDeserializationTest {
    companion object {
        private val TEST_JSON_STRING = """
{
    "id": 6,
    "title": "QA  ☾⋆",
    "description": "The Challenge! Ho-ho-ho!🎅\r\nHurry up and get yor prise!",
    "target_type": 14,
    "starting_date": "2023-11-02",
    "interval_duration_days": 1,
    "intervals_count": 1,
    "status": "not completed",
    "reward_link": null,
    "progress":
    [
        false
    ],
    "finish_date": "2023-11-03",
    "current_interval": null
}
        """.trimIndent()
    }

    @Test
    fun `Test Challenge deserialization`() {
        val json = NetworkModule.provideJson()
        val expected = Challenge(
            id = 6,
            title = "QA  ☾⋆",
            description = "The Challenge! Ho-ho-ho!🎅\r\nHurry up and get yor prise!",
            targetTypeValue = 14,
            startingDate = LocalDate.parse("2023-11-02"),
            intervalDurationDays = 1,
            intervalsCount = 1,
            statusValue = "not completed",
            rewardLink = null,
            progress = listOf(false),
            finishDate = LocalDate.parse("2023-11-03"),
            currentInterval = null
        )
        val decodedObject = json.decodeFromString(Challenge.serializer(), TEST_JSON_STRING)
        assertEquals(expected, decodedObject)
    }
}