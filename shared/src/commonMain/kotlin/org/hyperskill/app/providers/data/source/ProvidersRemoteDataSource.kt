package org.hyperskill.app.providers.data.source

import org.hyperskill.app.providers.domain.model.Provider

interface ProvidersRemoteDataSource {
    suspend fun getProviders(providersIds: List<Long>): Result<List<Provider>>
}