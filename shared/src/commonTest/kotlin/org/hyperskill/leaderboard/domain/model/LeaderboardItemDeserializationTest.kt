package org.hyperskill.leaderboard.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.leaderboard.domain.model.LeaderboardItem
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.users.domain.model.User

class LeaderboardItemDeserializationTest {
    companion object {
        private val TEST_JSON_STRING = """
{
    "user":
    {
        "id": 115368377,
        "avatar": "https://google.com/",
        "fullname": "John Appleseed"
    },
    "position": 1,
    "passed_problems": 10
}
        """.trimIndent()
    }

    @Test
    fun `Test LeaderboardItem deserialization`() {
        val json = NetworkModule.provideJson()
        val expected = LeaderboardItem(
            user = User(
                id = 115368377,
                avatar = "https://google.com/",
                fullname = "John Appleseed"
            ),
            position = 1,
            passedProblems = 10
        )
        val decodedObject = json.decodeFromString(LeaderboardItem.serializer(), TEST_JSON_STRING)
        assertEquals(expected, decodedObject)
    }
}