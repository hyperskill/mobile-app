package org.hyperskill.app.network.injection

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.auth.injection.AuthDataBuilder
import org.hyperskill.app.core.domain.url.HyperskillUrlBuilder
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.network.domain.model.AuthorizedClientDependencies
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

internal class NetworkComponentImpl(
    appGraph: AppGraph
) : NetworkComponent {
    override val endpointConfigInfo: NetworkEndpointConfigInfo =
        NetworkModule.provideEndpointConfigInfo(appGraph.commonComponent.buildKonfig)

    override val authMutex: Mutex =
        AuthDataBuilder.provideAuthorizationMutex()

    override val authorizationFlow: MutableSharedFlow<UserDeauthorized> =
        AuthDataBuilder.provideAuthorizationFlow()

    override val authSocialHttpClient: HttpClient =
        NetworkModule.provideClient(
            NetworkClientType.SOCIAL,
            endpointConfigInfo,
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json,
            appGraph.commonComponent.buildKonfig.buildVariant
        )

    override val authCredentialsHttpClient: HttpClient =
        NetworkModule.provideClient(
            NetworkClientType.CREDENTIALS,
            endpointConfigInfo,
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json,
            appGraph.commonComponent.buildKonfig.buildVariant
        )

    override val cookiesStorage: CookiesStorage =
        NetworkModule.provideCookiesStorage()

    override val authorizedHttpClient: HttpClient =
        NetworkModule.provideAuthorizedClient(
            dependencies = AuthorizedClientDependencies(
                networkEndpointConfigInfo = endpointConfigInfo,
                userAgentInfo = appGraph.commonComponent.userAgentInfo,
                json = appGraph.commonComponent.json,
                settings = appGraph.commonComponent.settings,
                buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
                authorizationFlow = authorizationFlow,
                authorizationMutex = authMutex,
                cookiesStorage = cookiesStorage,
                logger = appGraph.loggerComponent.logger
            )
        )

    override val frontendEventsUnauthorizedHttpClient: HttpClient =
        NetworkModule.provideFrontendEventsUnauthorizedClient(
            endpointConfigInfo,
            appGraph.commonComponent.userAgentInfo,
            appGraph.commonComponent.json,
            appGraph.commonComponent.buildKonfig.buildVariant,
            cookiesStorage
        )

    override val urlBuilder: HyperskillUrlBuilder
        get() = HyperskillUrlBuilder(endpointConfigInfo)
}