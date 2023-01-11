package org.hyperskill.app.debug.data.source

import org.hyperskill.app.debug.domain.model.EndpointConfigType

interface DebugCacheDataSource {
    fun getEndpointConfig(): EndpointConfigType
    fun setEndpointConfig(endpointConfigType: EndpointConfigType)
}