package org.hyperskill.app.android.network.injection

import com.russhwolf.settings.Settings
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.network.domain.model.NetworkClientType
import org.hyperskill.app.network.injection.NetworkModule

@Module
object AndroidNetworkModule {
    @Provides
    fun provideJson(): Json =
        NetworkModule.provideJson()

    @Provides
    @AuthSocialHttpClient
    fun provideAuthSocialHttpClient(
        userAgentInfo: UserAgentInfo,
        json: Json
    ): HttpClient =
        NetworkModule.provideClient(NetworkClientType.SOCIAL, userAgentInfo, json)

    @Provides
    @AuthCredentialsHttpClient
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
        settings: Settings,
        authorizationFlow: MutableSharedFlow<UserDeauthorized>,
        authorizationMutex: Mutex
    ): HttpClient =
        NetworkModule.provideAuthorizedClient(userAgentInfo, json, settings, authorizationFlow, authorizationMutex)

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