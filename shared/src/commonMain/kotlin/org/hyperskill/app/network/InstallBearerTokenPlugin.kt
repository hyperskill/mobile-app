package org.hyperskill.app.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.utils.io.errors.IOException
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.network.domain.model.AuthorizedClientDependencies
import org.hyperskill.app.network.plugin.bearer_token.BearerTokenHttpClientPlugin
import org.hyperskill.app.network.plugin.bearer_token.TokenRefreshResult

private const val LOG_TAG = "BearerTokenPlugin"

internal fun HttpClientConfig<*>.installBearerTokenPlugin(
    dependencies: AuthorizedClientDependencies,
    tokenSocialAuthClient: HttpClient,
    tokenCredentialsAuthClient: HttpClient
) {
    val logger = dependencies.logger.withTag(LOG_TAG)
    install(BearerTokenHttpClientPlugin) {
        authMutex = dependencies.authorizationMutex
        tokenProvider = { BearerTokenHandler.getAccessToken(dependencies.json, dependencies.settings) }
        tokenUpdater = {
            BearerTokenHandler.refreshAccessToken(
                json = dependencies.json,
                settings = dependencies.settings,
                tokenCredentialsAuthClient = tokenCredentialsAuthClient,
                tokenSocialAuthClient = tokenSocialAuthClient
            ).fold(
                onSuccess = {
                    logger.d { "Successfully refresh access token" }
                    TokenRefreshResult.Success
                },
                onFailure = { e ->
                    logger.e(e) { "Failed to refresh access token!" }
                    TokenRefreshResult.Failed(
                        shouldDeauthorizeUser = e !is IOException
                    )
                }
            )
        }
        tokenExpirationChecker = {
            val isExpired =
                BearerTokenHandler.checkAccessTokenIsExpired(dependencies.json, dependencies.settings)
            if (isExpired) {
                logger.e { "Access token expired" }
            }
            isExpired
        }
        tokenRefreshFailedReporter = {
            dependencies
                .authorizationFlow
                .tryEmit(UserDeauthorized(reason = UserDeauthorized.Reason.TOKEN_REFRESH_FAILURE))
        }
    }
}