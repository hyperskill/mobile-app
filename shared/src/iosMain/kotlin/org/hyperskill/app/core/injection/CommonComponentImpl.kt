package org.hyperskill.app.core.injection

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.config.BuildKonfigModule
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.Platform
import org.hyperskill.app.core.remote.UserAgentInfo
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.ResourceProviderImpl
import org.hyperskill.app.network.injection.NetworkModule
import platform.Foundation.NSUserDefaults

class CommonComponentImpl(
    buildVariant: BuildVariant,
    override val userAgentInfo: UserAgentInfo
) : CommonComponent {
    override val json: Json =
        NetworkModule.provideJson()

    override val settings: Settings =
        AppleSettings(NSUserDefaults.standardUserDefaults)

    override val resourceProvider: ResourceProvider =
        ResourceProviderImpl()

    override val platform: Platform =
        Platform()

    override val buildKonfig: BuildKonfig =
        BuildKonfigModule.provideBuildKonfig(buildVariant, settings)
}