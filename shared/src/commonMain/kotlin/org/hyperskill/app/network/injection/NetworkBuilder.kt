package org.hyperskill.app.network.injection

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
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.domain.model.NetworkClientType

object NetworkBuilder {
    private const val AUTHORIZATION_HEADER = "Authorization"

    internal fun buildAuthClient(networkClientType: NetworkClientType, userAgentInfo: UserAgentInfo, json: Json): HttpClient {
        val (clientId, clientSecret) = when (networkClientType) {
            NetworkClientType.SOCIAL ->
                BuildKonfig.OAUTH_CLIENT_ID to BuildKonfig.OAUTH_CLIENT_SECRET
            NetworkClientType.CREDENTIALS ->
                BuildKonfig.CREDENTIALS_CLIEND_ID to BuildKonfig.CREDENTIALS_CLIENT_SECRET
        }

        return provideClientFromBasicAuthCredentials(userAgentInfo, json, constructBasicAuthValue(clientId, clientSecret))
    }

    internal fun buildAuthorizedClient(
        userAgentInfo: UserAgentInfo,
        json: Json,
        settings: Settings,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        authorizationMutex: Mutex,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        HttpClient {
            val tokenSocialAuthClient = buildAuthClient(
                NetworkClientType.SOCIAL,
                userAgentInfo,
                json
            )

            val tokenCredentialsAuthClient = buildAuthClient(
                NetworkClientType.CREDENTIALS,
                userAgentInfo,
                json
            )

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildKonfig.HOST
                }
            }
            install(HttpCookiesPlugin) {
                storage = cookiesStorage
                shouldSendCookiesForRequest = false
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
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
                    authorizationFlow.tryEmit(UserDeauthorized)
                }
            }
        }

    internal fun buildFrontendEventsUnauthorizedClient(
        userAgentInfo: UserAgentInfo,
        json: Json,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        HttpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildKonfig.HOST
                }
            }
            install(HttpCookiesPlugin) {
                storage = cookiesStorage
                shouldSendCookiesForRequest = true
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(UserAgent) {
                agent = userAgentInfo.toString()
            }
        }

    private fun provideClientFromBasicAuthCredentials(userAgentInfo: UserAgentInfo, json: Json, credentials: String) =
        HttpClient {
            defaultRequest {
                headers {
                    append(AUTHORIZATION_HEADER, credentials)
                }
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildKonfig.HOST
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
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