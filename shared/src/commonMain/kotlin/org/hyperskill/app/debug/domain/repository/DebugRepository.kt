package org.hyperskill.app.debug.domain.repository

import org.hyperskill.app.debug.domain.model.EndpointConfigType

interface DebugRepository {
    fun getEndpointConfig(): EndpointConfigType
    fun setEndpointConfig(endpointConfigType: EndpointConfigType)
}