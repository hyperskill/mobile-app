package org.hyperskill.app.stages.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.stages.data.repository.StagesRepositoryImpl
import org.hyperskill.app.stages.data.source.StagesRemoteDataSource
import org.hyperskill.app.stages.domain.interactor.StagesInteractor
import org.hyperskill.app.stages.domain.repository.StagesRepository
import org.hyperskill.app.stages.remote.StagesRemoteDataSourceImpl

class StagesDataComponentImpl(appGraph: AppGraph) : StagesDataComponent {
    private val stagesRemoteDataSource: StagesRemoteDataSource =
        StagesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val stagesRepository: StagesRepository
        get() = StagesRepositoryImpl(stagesRemoteDataSource)

    override val stagesInteractor: StagesInteractor
        get() = StagesInteractor(stagesRepository)
}