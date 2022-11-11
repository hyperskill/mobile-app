package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlPath

class HyperskillUrlBuilderTest {

    @Test
    fun buildUrlsTest() {
        val paths = listOf(
            HyperskillUrlPath.Index(),
            HyperskillUrlPath.Profile(1),
            HyperskillUrlPath.Register(),
            HyperskillUrlPath.ResetPassword(),
            HyperskillUrlPath.StudyPlan(),
            HyperskillUrlPath.Track(1),
            HyperskillUrlPath.DeleteAccount()
        )

        for (path in paths) {
            val url = HyperskillUrlBuilder.build(path)

            val expected = when (path) {
                is HyperskillUrlPath.Index -> BuildKonfig.BASE_URL
                is HyperskillUrlPath.Profile -> "${BuildKonfig.BASE_URL}profile/1"
                is HyperskillUrlPath.Register -> "${BuildKonfig.BASE_URL}register"
                is HyperskillUrlPath.ResetPassword -> "${BuildKonfig.BASE_URL}accounts/password/reset"
                is HyperskillUrlPath.StudyPlan -> "${BuildKonfig.BASE_URL}study-plan"
                is HyperskillUrlPath.Track -> "${BuildKonfig.BASE_URL}tracks/1"
                is HyperskillUrlPath.DeleteAccount -> "${BuildKonfig.BASE_URL}delete-account"
            }

            assertEquals(expected, url.toString())
        }
    }
}