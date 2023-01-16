package org.hyperskill.app.config

import com.russhwolf.settings.Settings
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.debug.cache.DebugCacheDataSourceImpl

object BuildKonfigModule {
    fun provideBuildKonfig(
        buildVariant: BuildVariant,
        settings: Settings
    ): BuildKonfig =
        BuildKonfig(
            buildVariant = buildVariant,
            endpointConfigType = DebugCacheDataSourceImpl(settings).getEndpointConfig()
        )
}