package org.hyperskill.app.network.injection

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.UserAgent
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.auth.providers.bearer
import io.ktor.client.features.auth.providers.BearerTokens
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.client.request.forms.submitForm
import io.ktor.http.URLProtocol
import io.ktor.http.Parameters
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.config.BuildKonfig

object NetworkModule {
    fun provideJson(): Json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
//                contextual(UTCDateSerializer())
            }
        }

    // TODO Stub, will be removed with user list feature
    fun provideClient(json: Json): HttpClient =
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
        }

    fun provideAuthorizedClient(
        userAgentValue: String,
        json: Json,
        settings: Settings
    ): HttpClient =
        HttpClient {
            val tokenClient = provideAuthClient(userAgentValue, json)

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildKonfig.HOST
                }
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(UserAgent) {
                agent = userAgentValue
            }
            install(Auth) {
                basic {
                    sendWithoutRequest = true
                    username = BuildKonfig.OAUTH_CLIENT_ID
                    password = BuildKonfig.OAUTH_CLIENT_SECRET
                }

                bearer {
                    loadTokens {
                        val authResponse = json.decodeFromString<AuthResponse>(settings.getString(AuthCacheKeyValues.AUTH_RESPONSE, ""))
                        BearerTokens(
                            accessToken = authResponse.accessToken,
                            refreshToken = authResponse.refreshToken
                        )
                    }

                    sendWithoutRequest { true }

                    refreshTokens {
                        val authResponse = json.decodeFromString<AuthResponse>(settings.getString(AuthCacheKeyValues.AUTH_RESPONSE, ""))
                        val refreshTokenInfo = tokenClient.submitForm<AuthResponse>(
                            url = "/oauth2/token/",
                            formParameters = Parameters.build {
                                append("grant_type", "refresh_token")
                                append("refresh_token", authResponse.refreshToken)
                            }
                        )
                        settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(refreshTokenInfo))
                        BearerTokens(
                            accessToken = refreshTokenInfo.accessToken,
                            refreshToken = refreshTokenInfo.refreshToken
                        )
                    }
                }
            }
        }

    fun provideAuthClient(userAgentValue: String, json: Json): HttpClient =
        HttpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildKonfig.HOST
                }
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(Auth) {
                basic {
                    sendWithoutRequest = true
                    username = BuildKonfig.OAUTH_CLIENT_ID
                    password = BuildKonfig.OAUTH_CLIENT_SECRET
                }
            }
            install(UserAgent) {
                agent = userAgentValue
            }
        }
}