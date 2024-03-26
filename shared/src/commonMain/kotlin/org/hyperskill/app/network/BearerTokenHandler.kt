package org.hyperskill.app.network

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.network.domain.model.NetworkClientType

internal object BearerTokenHandler {
    private const val REFRESH_TOKEN_URL = "/oauth2/token/"

    private const val GRAND_TYPE_PARAM_KEY = "grant_type"
    private const val GRAND_TYPE_PARAM_VALUE = "refresh_token"

    private const val REFRESH_TOKEN_PARAM_KEY = "refresh_token"

    suspend fun refreshBearerToken(
        json: Json,
        settings: Settings,
        tokenCredentialsAuthClient: HttpClient,
        tokenSocialAuthClient: HttpClient,
        refreshToken: String
    ): Result<AuthResponse> =
        runCatching {
            refreshBearerTokenInternal(
                httpClient = getRefreshTokenClient(
                    settings = settings,
                    tokenCredentialsAuthClient = tokenCredentialsAuthClient,
                    tokenSocialAuthClient = tokenSocialAuthClient
                ),
                refreshToken = refreshToken
            )
        }.onSuccess {
            saveAuthResponse(json, settings, it)
        }

    private suspend fun refreshBearerTokenInternal(
        httpClient: HttpClient,
        refreshToken: String
    ): AuthResponse =
        httpClient.submitForm(
            url = REFRESH_TOKEN_URL,
            formParameters = Parameters.build {
                append(GRAND_TYPE_PARAM_KEY, GRAND_TYPE_PARAM_VALUE)
                append(REFRESH_TOKEN_PARAM_KEY, refreshToken)
            }
        ).body<AuthResponse>()

    private fun saveAuthResponse(
        json: Json,
        settings: Settings,
        authResponse: AuthResponse
    ) {
        with(settings) {
            putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse))
            putLong(
                AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP,
                Clock.System.now().epochSeconds
            )
        }
    }

    private fun getRefreshTokenClient(
        settings: Settings,
        tokenCredentialsAuthClient: HttpClient,
        tokenSocialAuthClient: HttpClient
    ): HttpClient {
        val currentAuthClientType =
            settings.getInt(AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL)
        return when (NetworkClientType.values()[currentAuthClientType]) {
            NetworkClientType.CREDENTIALS ->
                tokenCredentialsAuthClient
            NetworkClientType.SOCIAL ->
                tokenSocialAuthClient
        }
    }
}