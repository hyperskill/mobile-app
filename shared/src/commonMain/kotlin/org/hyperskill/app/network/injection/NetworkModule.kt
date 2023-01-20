package org.hyperskill.app.network.injection

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.remote.UserAgentInfo
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
        networkEndpointConfigInfo: NetworkEndpointConfigInfo,
        userAgentInfo: UserAgentInfo,
        json: Json,
        settings: Settings,
        buildVariant: BuildVariant,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        authorizationMutex: Mutex,
        cookiesStorage: CookiesStorage
    ): HttpClient =
        NetworkBuilder.buildAuthorizedClient(
            networkEndpointConfigInfo,
            userAgentInfo,
            json,
            settings,
            buildVariant,
            authorizationFlow,
            authorizationMutex,
            cookiesStorage
        )

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