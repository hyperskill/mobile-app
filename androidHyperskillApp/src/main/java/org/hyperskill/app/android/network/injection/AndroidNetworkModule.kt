package org.hyperskill.app.android.network.injection

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import org.hyperskill.app.network.injection.NetworkModule
import javax.inject.Singleton

@Module
object AndroidNetworkModule {
    @Provides
    fun provideJson(): Json =
        NetworkModule.provideJson()

    @Provides
    fun provideHttpClient(json: Json) =
        NetworkModule.provideClient(json)
}