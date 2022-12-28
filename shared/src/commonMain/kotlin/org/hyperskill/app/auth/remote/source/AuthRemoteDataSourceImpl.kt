package org.hyperskill.app.auth.remote.source

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.domain.exception.AuthCredentialsException
import org.hyperskill.app.auth.domain.exception.AuthSocialException
import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.auth.remote.model.AuthSocialErrorResponse
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

class AuthRemoteDataSourceImpl(
    private val authCacheMutex: Mutex,
    private val deauthorizationFlow: Flow<UserDeauthorized>,
    private val authSocialHttpClient: HttpClient,
    private val authCredentialsHttpClient: HttpClient,
    private val networkEndpointConfigInfo: NetworkEndpointConfigInfo,
    private val json: Json,
    private val settings: Settings
) : AuthRemoteDataSource {
    override fun observeUserDeauthorization(): Flow<UserDeauthorized> =
        deauthorizationFlow

    override suspend fun authWithSocial(
        authCode: String,
        idToken: String?,
        socialProvider: SocialAuthProvider
    ): Result<Unit> =
        if (socialProvider.isSdk) {
            authWithSocialToken(authCode, idToken, socialProvider.title)
        } else {
            authWithCode(authCode)
        }

    private suspend fun authWithSocialToken(authCode: String, idToken: String?, providerName: String): Result<Unit> =
        kotlin.runCatching {
            val httpResponse =
                authSocialHttpClient
                    .submitForm(
                        url = "/oauth2/social-token/",
                        formParameters = Parameters.build {
                            append("provider", providerName)
                            append("code", authCode)
                            append("grant_type", "authorization_code")
                            append("redirect_uri", networkEndpointConfigInfo.redirectUri)
                            if (idToken != null) {
                                append("id_token", idToken)
                            }
                        }
                    )
            resolveSocialAuthHttpResponse(httpResponse)
        }

    private suspend fun authWithCode(authCode: String): Result<Unit> =
        kotlin.runCatching {
            val httpResponse =
                authSocialHttpClient
                    .submitForm(
                        url = "/oauth2/token/",
                        formParameters = Parameters.build {
                            append("code", authCode)
                            append("grant_type", "authorization_code")
                            append("redirect_uri", networkEndpointConfigInfo.redirectUri)
                        }
                    )
            resolveSocialAuthHttpResponse(httpResponse)
        }

    override suspend fun authWithEmail(email: String, password: String): Result<Unit> =
        kotlin.runCatching {
            val httpResponse = authCredentialsHttpClient
                .submitForm(
                    url = "/oauth2/token/",
                    formParameters = Parameters.build {
                        append("username", email)
                        append("password", password)
                        append("grant_type", "password")
                        append("redirect_uri", networkEndpointConfigInfo.redirectUri)
                    }
                )

            if (httpResponse.status == HttpStatusCode.OK) {
                httpResponse
                    .body<AuthResponse>()
                    .also { authResponse ->
                        cacheAuthResponseInformation(authResponse, NetworkClientType.CREDENTIALS)
                    }
            } else {
                throw AuthCredentialsException(authCredentialsError = AuthCredentialsError.ERROR_CREDENTIALS_AUTH)
            }
        }

    private suspend fun resolveSocialAuthHttpResponse(httpResponse: HttpResponse) {
        if (httpResponse.status == HttpStatusCode.OK) {
            httpResponse
                .body<AuthResponse>()
                .also { authResponse ->
                    cacheAuthResponseInformation(authResponse, NetworkClientType.SOCIAL)
                }
        } else {
            val authErrorBody = httpResponse.body<AuthSocialErrorResponse>()
            val error = AuthSocialError
                .values()
                .find { it.name.lowercase() == authErrorBody.error }
                ?: AuthSocialError.CONNECTION_PROBLEM

            throw AuthSocialException(authSocialError = error)
        }
    }

    private suspend fun cacheAuthResponseInformation(authResponse: AuthResponse, networkClientType: NetworkClientType) {
        authCacheMutex.withLock {
            settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse))
            settings.putLong(AuthCacheKeyValues.AUTH_ACCESS_TOKEN_TIMESTAMP, Clock.System.now().epochSeconds)
            settings.putInt(AuthCacheKeyValues.AUTH_SOCIAL_ORDINAL, networkClientType.ordinal)
        }
    }
}