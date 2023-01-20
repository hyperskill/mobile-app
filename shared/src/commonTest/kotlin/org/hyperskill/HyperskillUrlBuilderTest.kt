package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

class HyperskillUrlBuilderTest {

    @Test
    fun buildUrlsTest() {
        val buildKonfig = BuildKonfig(
            buildVariant = BuildVariant.DEBUG,
            endpointConfigType = EndpointConfigType.PRODUCTION
        )
        val networkEndpointConfigInfo = NetworkEndpointConfigInfo(
            buildKonfig.baseUrl,
            buildKonfig.host,
            buildKonfig.oauthClientId,
            buildKonfig.oauthClientSecret,
            buildKonfig.redirectUri,
            buildKonfig.credentialsClientId,
            buildKonfig.credentialsClientSecret
        )

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
            val url = HyperskillUrlBuilder(networkEndpointConfigInfo).build(path)

            val expected = when (path) {
                is HyperskillUrlPath.Index -> networkEndpointConfigInfo.baseUrl
                is HyperskillUrlPath.Profile -> "${networkEndpointConfigInfo.baseUrl}profile/1"
                is HyperskillUrlPath.Register -> "${networkEndpointConfigInfo.baseUrl}register"
                is HyperskillUrlPath.ResetPassword -> "${networkEndpointConfigInfo.baseUrl}accounts/password/reset"
                is HyperskillUrlPath.StudyPlan -> "${networkEndpointConfigInfo.baseUrl}study-plan"
                is HyperskillUrlPath.Track -> "${networkEndpointConfigInfo.baseUrl}tracks/1"
                is HyperskillUrlPath.DeleteAccount -> "${networkEndpointConfigInfo.baseUrl}delete-account"
            }

            assertEquals(expected, url.toString())
        }
    }
}