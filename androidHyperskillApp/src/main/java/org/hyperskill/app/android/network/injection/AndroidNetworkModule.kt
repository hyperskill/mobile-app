package org.hyperskill.app.android.network.injection

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.network.injection.NetworkModule
import javax.inject.Named

@Module
object AndroidNetworkModule {
    @Provides
    fun provideJson(): Json =
        NetworkModule.provideJson()

    @Provides
    @StubHttpClient
    fun provideStubHttpClient(
        json: Json
    ): HttpClient =
        NetworkModule.provideClient(json)

    @Provides
    @AuthHttpClient
    fun provideAuthHttpClient(
        @Named("UserAgentValue")
        userAgentValue: String,
        json: Json
    ): HttpClient =
        NetworkModule.provideAuthClient(userAgentValue, json)

    @Provides
    @AuthorizedHttpClient
    fun provideHttpClient(
        @Named("UserAgentValue")
        userAgentValue: String,
        json: Json,
        settings: Settings
    ): HttpClient =
        NetworkModule.provideAuthorizedClient(userAgentValue, json, settings)

    @Provides
    @JvmStatic
    @Named("UserAgentValue")
    fun provideUserAgentValue(): String =
        "Hyperskill-Mobile/${BuildConfig.VERSION_NAME} (Android ${android.os.Build.VERSION.SDK_INT}) build/${BuildConfig.VERSION_CODE} package/${BuildConfig.APPLICATION_ID}"
}