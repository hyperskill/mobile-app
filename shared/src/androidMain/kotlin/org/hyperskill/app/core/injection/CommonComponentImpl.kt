package org.hyperskill.app.core.injection

import android.app.Application
import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.config.BuildKonfigModule
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.Platform
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.ResourceProviderImpl
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.network.injection.NetworkModule

class CommonComponentImpl(
    application: Application,
    buildVariant: BuildVariant,
    override val userAgentInfo: UserAgentInfo
) : CommonComponent {
    override val json: Json =
        NetworkModule.provideJson()

    override val settings: Settings =
        if (BuildConfig.DEBUG) {
            AndroidSettings(application.getSharedPreferences("app_preferences", Context.MODE_PRIVATE))
        } else {
            AndroidSettings(
                EncryptedSharedPreferences.create(
                    application,
                    "app_preferences",
                    getMasterKey(application),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            )
        }

    override val resourceProvider: ResourceProvider =
        ResourceProviderImpl(application)

    override val dateFormatter: DateFormatter =
        DateFormatter(resourceProvider)

    override val platform: Platform =
        Platform()

    override val buildKonfig: BuildKonfig =
        BuildKonfigModule.provideBuildKonfig(buildVariant, settings)

    private fun getMasterKey(context: Context): MasterKey =
        MasterKey
            .Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
}