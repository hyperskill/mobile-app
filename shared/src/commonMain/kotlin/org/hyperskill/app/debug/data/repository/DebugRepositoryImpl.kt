package org.hyperskill.app.debug.data.repository

import org.hyperskill.app.debug.data.source.DebugCacheDataSource
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.domain.repository.DebugRepository

class DebugRepositoryImpl(
    private val debugCacheDataSource: DebugCacheDataSource
) : DebugRepository {
    override fun getEndpointConfig(): EndpointConfigType =
        debugCacheDataSource.getEndpointConfig()

    override fun setEndpointConfig(endpointConfigType: EndpointConfigType) {
        debugCacheDataSource.setEndpointConfig(endpointConfigType)
    }
}