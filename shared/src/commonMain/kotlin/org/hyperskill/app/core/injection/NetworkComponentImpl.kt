package org.hyperskill.app.core.injection

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.injection.AuthDataBuilder
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.injection.NetworkModule

class NetworkComponentImpl(
    appGraph: AppGraph
) : NetworkComponentManual {

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

    override val authorizedHttpClient: HttpClient =
        NetworkModule.provideAuthorizedClient(
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings,
            authorizationFlow,
            authMutex
        )
}