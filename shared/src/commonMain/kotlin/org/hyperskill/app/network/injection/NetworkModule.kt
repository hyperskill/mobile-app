package org.hyperskill.app.network.injection

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.logging.SIMPLE
import io.ktor.http.URLProtocol
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.hyperskill.app.config.BuildKonfig

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
        json: Json
    ): HttpClient =
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = BuildKonfig.HOST
                }
            }
        }
}