package org.hyperskill.app.network.injection

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.NetworkBuilder
import org.hyperskill.app.network.domain.model.AuthorizedClientDependencies
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

object NetworkModule {
    fun provideJson(): Json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
//                contextual(UTCDateSerializer())
            }
        }

    fun provideEndpointConfigInfo(buildKonfig: BuildKonfig): NetworkEndpointConfigInfo =
        NetworkBuilder.buildEndpointConfigInfo(buildKonfig)

    fun provideClient(
        networkClientType: NetworkClientType,
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        buildVariant: BuildVariant
    ): HttpClient =
        NetworkBuilder.buildAuthClient(networkClientType, networkEndpointConfigInfo, userAgentInfo, json, buildVariant)

    fun provideAuthorizedClient(
        dependencies: AuthorizedClientDependencies
    ): HttpClient =
        NetworkBuilder.buildAuthorizedClient(dependencies)

    fun provideFrontendEventsUnauthorizedClient(
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        buildVariant: BuildVariant,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        NetworkBuilder.buildFrontendEventsUnauthorizedClient(
            networkEndpointConfigInfo,
            userAgentInfo,
            json,
            buildVariant,
            cookiesStorage
        )

    fun provideCookiesStorage(): CookiesStorage =
        AcceptAllCookiesStorage()
}