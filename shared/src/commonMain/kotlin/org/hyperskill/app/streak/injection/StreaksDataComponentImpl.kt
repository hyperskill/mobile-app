package org.hyperskill.app.streak.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.streak.data.repository.StreaksRepositoryImpl
import org.hyperskill.app.streak.data.source.StreaksRemoteDataSource
import org.hyperskill.app.streak.domain.interactor.StreaksInteractor
import org.hyperskill.app.streak.domain.repository.StreaksRepository
import org.hyperskill.app.streak.remote.StreaksRemoteDataSourceImpl

class StreaksDataComponentImpl(appGraph: AppGraph) : StreaksDataComponent {
    private val streaksRemoteDataSource: StreaksRemoteDataSource =
        StreaksRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val streaksRepository: StreaksRepository = StreaksRepositoryImpl(streaksRemoteDataSource)

    override val streaksInteractor: StreaksInteractor
        get() = StreaksInteractor(streaksRepository)
}