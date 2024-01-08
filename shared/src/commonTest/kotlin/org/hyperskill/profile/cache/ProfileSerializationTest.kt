package org.hyperskill.profile.cache

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.network.injection.NetworkModule
import org.hyperskill.app.profile.domain.model.Gamification
import org.hyperskill.app.profile.domain.model.Profile

class ProfileSerializationTest {

    companion object {
        private const val TEST_JSON = """
            {
              "id": 12345678,
              "avatar": "https://example.com/avatar.jpg",
              "bio": "Just a bio",
              "fullname": "John Doe",
              "gamification": {
                "hypercoins": 414,
                "passed_projects": 0
              },
              "completed_tracks": [1, 2, 3, 4, 5],
              "country": null,
              "languages": null,
              "experience": "5 years",
              "github_username": "johndoe",
              "linkedin_username": "johndoe",
              "twitter_username": "johndoetwitter",
              "reddit_username": "jdoe",
              "facebook_username": "jdoefacebook",
              "daily_step": null,
              "is_guest": false,
              "is_staff": true,
              "track_id": null,
              "track_title": null,
              "project": null,
              "is_beta": false,
              "timezone": null,
              "notification_hour": null,
              "features": {
                "feature1": true,
                "feature2": false
              }
            }
        """

        private val EXPECTED_PROFILE: Profile =
            Profile(
                id = 12345678L,
                avatar = "https://example.com/avatar.jpg",
                bio = "Just a bio",
                fullname = "John Doe",
                gamification = Gamification(
                    hypercoinsBalance = 414,
                    passedProjectsCount = 0
                ),
                completedTracks = listOf(1L, 2L, 3L, 4L, 5L),
                country = null,
                languages = null,
                experience = "5 years",
                githubUsername = "johndoe",
                linkedinUsername = "johndoe",
                twitterUsername = "johndoetwitter",
                redditUsername = "jdoe",
                facebookUsername = "jdoefacebook",
                dailyStep = null,
                isGuest = false,
                isStaff = true,
                trackId = null,
                trackTitle = null,
                projectId = null,
                isBeta = false,
                timeZone = null,
                notificationHour = null,
                featuresMap = mapOf("feature1" to true, "feature2" to false)
            )
    }
    @Test
    fun `Serialized profile should be deserialized normally`() {
        val json = NetworkModule.provideJson()
        val actualModel = json.decodeFromString(Profile.serializer(), TEST_JSON)
        assertEquals(EXPECTED_PROFILE, actualModel)
    }
}