package org.hyperskill.app.debug.domain.interactor

import org.hyperskill.app.debug.domain.model.DebugSettings
import org.hyperskill.app.debug.domain.model.EndpointConfigType
import org.hyperskill.app.debug.domain.repository.DebugRepository

class DebugInteractor(
    private val debugRepository: DebugRepository
) {
    fun fetchDebugSettings(): DebugSettings =
        DebugSettings(endpointConfigType = debugRepository.getEndpointConfig())

    fun updateEndpointConfig(endpointConfigType: EndpointConfigType) {
        debugRepository.setEndpointConfig(endpointConfigType)
    }
}