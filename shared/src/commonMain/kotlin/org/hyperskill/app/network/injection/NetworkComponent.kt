package org.hyperskill.app.network.injection

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.auth.domain.model.UserDeauthorized

interface NetworkComponent {
    val authMutex: Mutex
    val authorizationFlow: MutableSharedFlow<UserDeauthorized>
    val authSocialHttpClient: HttpClient
    val authCredentialsHttpClient: HttpClient
    val authorizedHttpClient: HttpClient
    val frontendEventsUnauthorizedHttpClient: HttpClient
    val cookiesStorage: CookiesStorage
}