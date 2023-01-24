package org.hyperskill.app.network

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.http.Parameters
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.auth.remote.source.BearerTokenHttpClientPlugin
import org.hyperskill.app.auth.remote.source.HttpCookiesPlugin
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
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

    fun buildAuthorizedClient(
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        settings: Settings,
        buildVariant: BuildVariant,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        authorizationMutex: Mutex,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        PreconfiguredHttpClient {
            val tokenSocialAuthClient = buildAuthClient(
                NetworkClientType.SOCIAL,
                networkEndpointConfigInfo,
                userAgentInfo,
                json,
                buildVariant
            )

            val tokenCredentialsAuthClient = buildAuthClient(
                NetworkClientType.CREDENTIALS,
                networkEndpointConfigInfo,
                userAgentInfo,
                json,
                buildVariant
            )

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = networkEndpointConfigInfo.host
                }
            }
            install(HttpCookiesPlugin) {
                storage = cookiesStorage
                shouldSendCookiesForRequest = false
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
            install(BearerTokenHttpClientPlugin) {
                authMutex = authorizationMutex
                tokenHeaderName = AUTHORIZATION_HEADER
                tokenProvider = {
                    getAuthResponse(json, settings)?.accessToken
                }
                tokenUpdater = {
                    val refreshToken = getAuthResponse(json, settings)?.refreshToken
                    if (refreshToken != null) {
                        val refreshTokenResult = kotlin.runCatching {
                            val currentAuthClientType =
                                settings.getInt(AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL)
                            val tokenClient =
                                when (NetworkClientType.values()[currentAuthClientType]) {
                                    NetworkClientType.CREDENTIALS ->
                                        tokenCredentialsAuthClient
                                    NetworkClientType.SOCIAL ->
                                        tokenSocialAuthClient
                                }

                            tokenClient.submitForm(
                                url = "/oauth2/token/",
                                formParameters = Parameters.build {
                                    append("grant_type", "refresh_token")
                                    append("refresh_token", refreshToken)
                                }
                            ).body<AuthResponse>()
                        }
                        refreshTokenResult.fold(
                            onSuccess = {
                                settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(it))
                                settings.putLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, Clock.System.now().epochSeconds)
                                true
                            },
                            onFailure = {
                                false
                            }
                        )
                    } else {
                        false
                    }
                }
                tokenExpirationChecker = {
                    val authResponse = getAuthResponse(json, settings)
                    val accessTokenTimestamp = settings.getLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, 0L)

                    if (authResponse == null || accessTokenTimestamp == 0L) {
                        true
                    } else {
                        val delta = Clock.System.now().epochSeconds - accessTokenTimestamp
                        delta > authResponse.expiresIn
                    }
                }
                tokenFailureReporter = {
                    authorizationFlow.tryEmit(UserDeauthorized(reason = UserDeauthorized.Reason.TOKEN_REFRESH_FAILURE))
                }
            }
        }

    fun buildFrontendEventsUnauthorizedClient(
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        buildVariant: BuildVariant,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        PreconfiguredHttpClient {
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
        PreconfiguredHttpClient {
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

    private fun getAuthResponse(json: Json, settings: Settings): AuthResponse? {
        val authResponseString = settings.getString(AuthCacheKeyValues.AUTH_RESPONSE, "")
        return if (authResponseString.isNotEmpty()) {
            json.decodeFromString<AuthResponse>(authResponseString)
        } else {
            null
        }
    }

    private fun constructBasicAuthValue(username: String, password: String): String {
        val authString = "$username:$password"
        val authBuf = authString.toByteArray(Charsets.UTF_8).encodeBase64()

        return "Basic $authBuf"
    }
}