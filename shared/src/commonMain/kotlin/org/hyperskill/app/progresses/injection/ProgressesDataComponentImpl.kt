package org.hyperskill.app.progresses.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progresses.data.repository.ProgressesRepositoryImpl
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.progresses.remote.ProgressesRemoteDataSourceImpl

class ProgressesDataComponentImpl(
    private val appGraph: AppGraph
) : ProgressesDataComponent {

    private val progressesRemoteDataSource: ProgressesRemoteDataSource =
        ProgressesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val progressesRepository: ProgressesRepository
        get() = ProgressesRepositoryImpl(
            progressesRemoteDataSource,
            appGraph.singletonRepositoriesComponent.trackProgressesCacheDataSource,
            appGraph.singletonRepositoriesComponent.projectProgressesCacheDataSource
        )

    override val progressesInteractor: ProgressesInteractor =
        ProgressesInteractor(progressesRepository)
}