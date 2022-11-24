package org.hyperskill.app.progresses.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progresses.data.repository.ProgressesRepositoryImpl
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.progresses.remote.ProgressesRemoteDataSourceImpl

class ProgressesDataComponentImpl(
    appGraph: AppGraph
) : ProgressesDataComponent {
    private val progressesRemoteDataSource: ProgressesRemoteDataSource =
        ProgressesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val progressesRepository: ProgressesRepository =
        ProgressesRepositoryImpl(progressesRemoteDataSource)

    override val progressesInteractor: ProgressesInteractor
        get() = ProgressesInteractor(progressesRepository)
}