package org.hyperskill.app.streaks.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.streaks.data.repository.StreaksRepositoryImpl
import org.hyperskill.app.streaks.data.source.StreaksRemoteDataSource
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import org.hyperskill.app.streaks.domain.repository.StreaksRepository
import org.hyperskill.app.streaks.remote.StreaksRemoteDataSourceImpl

class StreaksDataComponentImpl(appGraph: AppGraph) : StreaksDataComponent {
    private val streaksRemoteDataSource: StreaksRemoteDataSource =
        StreaksRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val streaksRepository: StreaksRepository = StreaksRepositoryImpl(streaksRemoteDataSource)

    override val streaksInteractor: StreaksInteractor
        get() = StreaksInteractor(streaksRepository)
}