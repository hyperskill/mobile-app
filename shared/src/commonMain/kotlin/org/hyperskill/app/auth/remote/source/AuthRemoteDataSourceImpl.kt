package org.hyperskill.app.auth.remote.source

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.config.BuildKonfig

class AuthRemoteDataSourceImpl(
    private val authHttpClient: HttpClient,
    private val json: Json,
    private val settings: Settings
) : AuthRemoteDataSource {
    override suspend fun authWithSocialToken(authCode: String, providerName: String): Result<Unit> =
        kotlin.runCatching {
            authHttpClient
                .submitForm<AuthResponse>(
                    url = "/oauth2/social-token/",
                    formParameters = Parameters.build {
                        append("provider", providerName)
                        append("code", authCode)
                        append("grant_type", "authorization_code")
                        append("redirect_uri", BuildKonfig.REDIRECT_URI)
                    }
                ).also { authResponse ->
                    settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse))
                    settings.putLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, Clock.System.now().epochSeconds)
                }
        }

    override suspend fun authWithCode(authCode: String): Result<Unit> =
        kotlin.runCatching {
            authHttpClient
                .submitForm<AuthResponse>(
                    url = "/oauth2/token/",
                    formParameters = Parameters.build {
                        append("code", authCode)
                        append("grant_type", "authorization_code")
                        append("redirect_uri", BuildKonfig.REDIRECT_URI)
                    }
                ).also { authResponse ->
                    settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse))
                    settings.putLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, Clock.System.now().epochSeconds)
                }
        }
}