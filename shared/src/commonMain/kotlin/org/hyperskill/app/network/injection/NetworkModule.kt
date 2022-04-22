package org.hyperskill.app.network.injection

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.domain.model.NetworkClientType

object NetworkModule {
    fun provideJson(): Json =
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
//                contextual(UTCDateSerializer())
            }
        }

    fun provideClient(
        networkClientType: NetworkClientType,
        userAgentInfo: UserAgentInfo,
        json: Json
    ): HttpClient =
        NetworkBuilder.buildAuthClient(networkClientType, userAgentInfo, json)

    // TODO Stub, will be removed with user list feature
    fun provideStubClient(json: Json): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(json)
            }
        }

    fun provideAuthorizedClient(
        userAgentInfo: UserAgentInfo,
        json: Json,
        settings: Settings,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>
    ): HttpClient =
        NetworkBuilder.buildAuthorizedClient(userAgentInfo, json, settings, authorizationFlow)
}