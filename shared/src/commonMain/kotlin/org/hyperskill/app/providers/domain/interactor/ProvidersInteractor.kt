package org.hyperskill.app.providers.domain.interactor

import org.hyperskill.app.providers.domain.model.Provider
import org.hyperskill.app.providers.domain.repository.ProvidersRepository

class ProvidersInteractor(
    private val providersRepository: ProvidersRepository
) {
    suspend fun getProviders(providersIds: List<Long>, forceLoadFromRemote: Boolean): Result<List<Provider>> =
        providersRepository.getProviders(providersIds, forceLoadFromRemote)

    suspend fun getProvider(providerId: Long, forceLoadFromRemote: Boolean): Result<Provider> =
        providersRepository.getProvider(providerId, forceLoadFromRemote)
}