package org.hyperskill.app.network.injection

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.injection.AuthDataBuilder
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.network.domain.model.NetworkClientType

class NetworkComponentImpl(
    appGraph: AppGraph
) : NetworkComponent {

    override val authMutex: Mutex =
        AuthDataBuilder.provideAuthorizationMutex()

    override val authorizationFlow: MutableSharedFlow<UserDeauthorized> =
        AuthDataBuilder.provideAuthorizationFlow()

    override val authSocialHttpClient: HttpClient =
        NetworkModule.provideClient(
            NetworkClientType.SOCIAL,
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json
        )

    override val authCredentialsHttpClient: HttpClient =
        NetworkModule.provideClient(
            NetworkClientType.CREDENTIALS,
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json
        )

    override val cookiesStorage: CookiesStorage =
        NetworkModule.provideCookiesStorage()

    override val authorizedHttpClient: HttpClient =
        NetworkModule.provideAuthorizedClient(
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings,
            authorizationFlow,
            authMutex,
            cookiesStorage
        )

    override val frontendEventsUnauthorizedHttpClient: HttpClient =
        NetworkModule.provideFrontendEventsUnauthorizedClient(
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json,
            cookiesStorage
        )
}