package org.hyperskill.app.network.injection

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.forms.submitForm
import io.ktor.http.URLProtocol
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.hyperskill.app.auth.remote.source.BearerTokenHttpClientPlugin
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.domain.model.NetworkClientType

object NetworkModule {
    fun provideJson(): Json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
//                contextual(UTCDateSerializer())
            }
        }

    fun provideClient(networkClientType: NetworkClientType, userAgentInfo: UserAgentInfo, json: Json): HttpClient =
        when (networkClientType) {
            NetworkClientType.SOCIAL ->
                provideClientFromBasicAuthCredentials(
                    userAgentInfo,
                    json,
                    BasicAuthCredentials(
                        username = BuildKonfig.OAUTH_CLIENT_ID,
                        password = BuildKonfig.OAUTH_CLIENT_SECRET
                    )
                )
            NetworkClientType.CREDENTIALS ->
                provideClientFromBasicAuthCredentials(
                    userAgentInfo,
                    json,
                    BasicAuthCredentials(
                        username = BuildKonfig.CREDENTIALS_CLIEND_ID,
                        password = BuildKonfig.CREDENTIALS_CLIENT_SECRET
                    )
                )
        }

    // TODO Stub, will be removed with user list feature
    fun provideStubClient(json: Json): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
        }

    fun provideAuthorizedClient(
        userAgentInfo: UserAgentInfo,
        json: Json,
        settings: Settings,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>
    ): HttpClient =
        HttpClient {
            val tokenClient = provideClient(
                NetworkClientType.values()[settings.getInt(AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL)],
                userAgentInfo,
                json
            )

            defaultRequest {
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
            install(BearerTokenHttpClientPlugin) {
                tokenHeaderName = "Authorization"
                tokenProvider = {
                    getAuthResponse(json, settings)?.accessToken
                }
                tokenUpdater = {
                    val refreshToken = getAuthResponse(json, settings)?.refreshToken
                    if (refreshToken != null) {
                        val refreshTokenResult = kotlin.runCatching {
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
                        val expireMillis = (authResponse.expiresIn - 50) * 1000
                        delta > expireMillis
                    }
                }
                tokenFailureReporter = {
                    authorizationFlow.tryEmit(UserDeauthorized)
                }
            }
        }

    private fun provideClientFromBasicAuthCredentials(userAgentInfo: UserAgentInfo, json: Json, basicCredentials: BasicAuthCredentials) =
        HttpClient {
            defaultRequest {
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
            install(Auth) {
                basic {
                    sendWithoutRequest { true }
                    credentials {
                        basicCredentials
                    }
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
}