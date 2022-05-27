package org.hyperskill.app.core.injection

import android.app.Application
import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.ResourceProviderImpl
import org.hyperskill.app.network.injection.NetworkModule

class CommonComponentImpl(
    application: Application,
    override val userAgentInfo: UserAgentInfo
) : CommonComponent {

    override val json: Json =
        NetworkModule.provideJson()

    override val settings: Settings =
        AndroidSettings(application.getSharedPreferences("app_preferences", Context.MODE_PRIVATE))

    override val resourceProvider: ResourceProvider =
        ResourceProviderImpl(application)
}