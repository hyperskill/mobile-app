package org.hyperskill.app.progresses.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progresses.cache.ProjectProgressesCacheDataSourceImpl
import org.hyperskill.app.progresses.cache.TrackProgressesCacheDataSourceImpl
import org.hyperskill.app.progresses.data.repository.ProgressesRepositoryImpl
import org.hyperskill.app.progresses.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progresses.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progresses.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.progresses.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.progresses.remote.ProgressesRemoteDataSourceImpl

class ProgressesDataComponentImpl(
    appGraph: AppGraph
) : ProgressesDataComponent {
    companion object {
        private val trackProgressesCacheDataSource: TrackProgressesCacheDataSource by lazy {
            TrackProgressesCacheDataSourceImpl(InMemoryRepositoryCache())
        }
        private val projectProgressesCacheDataSource: ProjectProgressesCacheDataSource by lazy {
            ProjectProgressesCacheDataSourceImpl(InMemoryRepositoryCache())
        }
    }

    private val progressesRemoteDataSource: ProgressesRemoteDataSource =
        ProgressesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val progressesRepository: ProgressesRepository
        get() = ProgressesRepositoryImpl(
            progressesRemoteDataSource,
            trackProgressesCacheDataSource,
            projectProgressesCacheDataSource
        )

    override val progressesInteractor: ProgressesInteractor
        get() = ProgressesInteractor(progressesRepository)
}