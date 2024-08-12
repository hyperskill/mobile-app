package org.hyperskill.app.debug.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.debug.data.source.DebugCacheDataSource
import org.hyperskill.app.debug.domain.model.EndpointConfigType

internal class DebugCacheDataSourceImpl(
    private val settings: Settings
) : DebugCacheDataSource {
    override fun getEndpointConfig(): EndpointConfigType {
        val defaultEndpointConfigType =
            if (BuildKonfig.IS_INTERNAL_TESTING == true) {
                EndpointConfigType.MAIN
            } else {
                EndpointConfigType.PRODUCTION
            }

        val cachedEndpointConfigName = settings.getString(
            key = DebugCacheKeyValues.ENDPOINT_CONFIG,
            defaultValue = defaultEndpointConfigType.name
        )

        return EndpointConfigType.valueOf(cachedEndpointConfigName)
    }

    override fun setEndpointConfig(endpointConfigType: EndpointConfigType) {
        settings.putString(DebugCacheKeyValues.ENDPOINT_CONFIG, endpointConfigType.name)
    }
}