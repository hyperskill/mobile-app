package org.hyperskill.app.auth.remote.source

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.cache.AuthCacheKeyValues
import org.hyperskill.app.auth.data.source.AuthRemoteDataSource
import org.hyperskill.app.auth.remote.model.AuthResponse

class AuthRemoteDataSourceImpl(
    private val authHttpClient: HttpClient,
    private val json: Json,
    private val settings: Settings
) : AuthRemoteDataSource {
    override suspend fun authWithCode(authCode: String): Result<Unit> =
        kotlin.runCatching {
            authHttpClient
                .submitForm<AuthResponse>(
                    url = "/oauth2/social-token/",
                    formParameters = Parameters.build {
                        append("grant_type", "authorization_code")
                        append("code", authCode)
                        append("redirect_uri", "https://hyperskill.org/")
                        append("provider", "google")
                    }
                ).also { authResponse -> settings.putString(AuthCacheKeyValues.AUTH_RESPONSE, json.encodeToString(authResponse)) }
        }
}