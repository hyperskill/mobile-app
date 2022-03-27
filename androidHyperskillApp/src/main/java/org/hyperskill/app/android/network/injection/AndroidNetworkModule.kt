package org.hyperskill.app.android.network.injection

import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.hyperskill.app.network.injection.NetworkModule

@Module
object AndroidNetworkModule {
    @Provides
    fun provideJson(): Json =
        NetworkModule.provideJson()

    @Provides
    fun provideHttpClient(json: Json): HttpClient =
        NetworkModule.provideClient(json)
}