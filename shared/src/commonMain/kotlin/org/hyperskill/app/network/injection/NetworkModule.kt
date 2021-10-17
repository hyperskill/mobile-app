package org.hyperskill.app.network.injection

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

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
        }
}