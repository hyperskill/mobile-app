package org.hyperskill.app.android.network.injection

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.injection.NetworkModule

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
        NetworkModule.provideStubClient(json)

    @Provides
    @AuthHttpClient
    fun provideAuthHttpClient(
        userAgentInfo: UserAgentInfo,
        json: Json
    ): HttpClient =
        NetworkModule.provideClient(NetworkClientType.SOCIAL, userAgentInfo, json)

    @Provides
    @CredentialsHttpClient
    fun provideCredentialsHttpClient(
        userAgentInfo: UserAgentInfo,
        json: Json
    ): HttpClient =
        NetworkModule.provideClient(NetworkClientType.CREDENTIALS, userAgentInfo, json)

    @Provides
    @AuthorizedHttpClient
    fun provideHttpClient(
        userAgentInfo: UserAgentInfo,
        json: Json,
        settings: Settings
    ): HttpClient =
        NetworkModule.provideAuthorizedClient(userAgentInfo, json, settings)

    @Provides
    @JvmStatic
    fun provideUserAgentValue(): UserAgentInfo =
        UserAgentInfo(
            BuildConfig.VERSION_NAME,
            "Android ${android.os.Build.VERSION.SDK_INT}",
            BuildConfig.VERSION_CODE.toString(),
            BuildConfig.APPLICATION_ID
        )
}