package org.hyperskill.app.auth.remote.source

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.network.domain.model.NetworkClientType

class AuthRemoteDataSourceImpl(
    private val deauthorizationFlow: Flow<UserDeauthorized>,
    private val authSocialHttpClient: HttpClient,
    private val authCredentialsHttpClient: HttpClient,
    private val json: Json,
    private val settings: Settings
) : AuthRemoteDataSource {
    override fun observeUserDeauthorization(): Flow<UserDeauthorized> =
        deauthorizationFlow

    override suspend fun authWithCode(authCode: String): Result<Unit> =
        kotlin.runCatching {
            authSocialHttpClient
                .submitForm<AuthResponse>(
                    url = "/oauth2/social-token/",
                    formParameters = Parameters.build {
                        append("provider", "google")
                        append("code", authCode)
                        append("grant_type", "authorization_code")
                        append("redirect_uri", BuildKonfig.REDIRECT_URI)
                    }
                ).also { authResponse ->
                    settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse))
                    settings.putLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, Clock.System.now().epochSeconds)
                    settings.putInt(AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL, NetworkClientType.SOCIAL.ordinal)
                }
        }

    override suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        kotlin.runCatching {
            authCredentialsHttpClient
                .submitForm<AuthResponse>(
                    url = "/oauth2/token/",
                    formParameters = Parameters.build {
                        append("username", email)
                        append("password", password)
                        append("grant_type", "password")
                        append("redirect_uri", BuildKonfig.REDIRECT_URI)
                    }
                ).also { authResponse ->
                    settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse))
                    settings.putLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, Clock.System.now().epochSeconds)
                    settings.putInt(AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL, NetworkClientType.CREDENTIALS.ordinal)
                }
        }
}