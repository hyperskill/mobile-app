package org.hyperskill.app.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.remote.source.HttpCookiesPlugin
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.domain.model.AuthorizedClientDependencies
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

internal object NetworkBuilder {
    private const val AUTHORIZATION_HEADER = "Authorization"

    fun buildEndpointConfigInfo(buildKonfig: BuildKonfig): NetworkEndpointConfigInfo =
        NetworkEndpointConfigInfo(
            baseUrl = buildKonfig.baseUrl,
            host = buildKonfig.host,
            oauthClientId = buildKonfig.oauthClientId,
            oauthClientSecret = buildKonfig.oauthClientSecret,
            redirectUri = buildKonfig.redirectUri,
            credentialsClientId = buildKonfig.credentialsClientId,
            credentialsClientSecret = buildKonfig.credentialsClientSecret
        )

    internal fun buildAuthClient(
        networkClientType: NetworkClientType,
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        buildVariant: BuildVariant
    ): HttpClient {
        val (clientId, clientSecret) = when (networkClientType) {
            NetworkClientType.SOCIAL ->
                networkEndpointConfigInfo.oauthClientId to networkEndpointConfigInfo.oauthClientSecret
            NetworkClientType.CREDENTIALS ->
                networkEndpointConfigInfo.credentialsClientId to networkEndpointConfigInfo.credentialsClientSecret
        }

        return provideClientFromBasicAuthCredentials(
            networkEndpointConfigInfo,
            userAgentInfo,
            json,
            buildVariant,
            constructBasicAuthValue(clientId, clientSecret)
        )
    }

    fun buildAuthorizedClient(dependencies: AuthorizedClientDependencies): HttpClient =
        PreconfiguredPlatformHttpClient {
            val tokenSocialAuthClient = buildAuthClient(
                NetworkClientType.SOCIAL,
                dependencies.networkEndpointConfigInfo,
                dependencies.userAgentInfo,
                dependencies.json,
                dependencies.buildVariant
            )

            val tokenCredentialsAuthClient = buildAuthClient(
                NetworkClientType.CREDENTIALS,
                dependencies.networkEndpointConfigInfo,
                dependencies.userAgentInfo,
                dependencies.json,
                dependencies.buildVariant
            )

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = dependencies.networkEndpointConfigInfo.host
                }
            }
            install(HttpCookiesPlugin) {
                storage = dependencies.cookiesStorage
                shouldSendCookiesForRequest = false
                this.networkEndpointConfigInfo = dependencies.networkEndpointConfigInfo
            }
            install(ContentNegotiation) {
                json(dependencies.json)
            }
            if (dependencies.buildVariant.isDebug()) {
                install(Logging) {
                    this.logger = Logger.SIMPLE
                    this.level = LogLevel.ALL
                }
            }
            install(UserAgent) {
                agent = dependencies.userAgentInfo.toString()
            }
            installBearerTokenPlugin(
                dependencies = dependencies,
                tokenSocialAuthClient = tokenSocialAuthClient,
                tokenCredentialsAuthClient = tokenCredentialsAuthClient
            )
        }

    fun buildFrontendEventsUnauthorizedClient(
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        buildVariant: BuildVariant,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        PreconfiguredPlatformHttpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = networkEndpointConfigInfo.host
                }
            }
            install(HttpCookiesPlugin) {
                storage = cookiesStorage
                shouldSendCookiesForRequest = true
                this.networkEndpointConfigInfo = networkEndpointConfigInfo
            }
            install(ContentNegotiation) {
                json(json)
            }
            if (buildVariant.isDebug()) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }
            install(UserAgent) {
                agent = userAgentInfo.toString()
            }
        }

    private fun provideClientFromBasicAuthCredentials(
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        buildVariant: BuildVariant,
        credentials: String
    ) =
        PreconfiguredPlatformHttpClient {
            defaultRequest {
                headers {
                    append(AUTHORIZATION_HEADER, credentials)
                }
                url {
                    protocol = URLProtocol.HTTPS
                    host = networkEndpointConfigInfo.host
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
            if (buildVariant.isDebug()) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }
            install(UserAgent) {
                agent = userAgentInfo.toString()
            }
        }

    private fun constructBasicAuthValue(username: String, password: String): String {
        val authString = "$username:$password"
        val authBuf = authString.toByteArray(Charsets.UTF_8).encodeBase64()

        return "Basic $authBuf"
    }
}