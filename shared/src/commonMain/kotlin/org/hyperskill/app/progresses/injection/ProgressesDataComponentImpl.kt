package org.hyperskill.app.progress_screen.injection

import org.hyperskill.app.core.data.repository_cache.InMemoryRepositoryCache
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.progress_screen.cache.ProjectProgressesCacheDataSourceImpl
import org.hyperskill.app.progress_screen.cache.TrackProgressesCacheDataSourceImpl
import org.hyperskill.app.progress_screen.data.repository.ProgressesRepositoryImpl
import org.hyperskill.app.progress_screen.data.source.ProgressesRemoteDataSource
import org.hyperskill.app.progress_screen.data.source.ProjectProgressesCacheDataSource
import org.hyperskill.app.progress_screen.data.source.TrackProgressesCacheDataSource
import org.hyperskill.app.progress_screen.domain.interactor.ProgressesInteractor
import org.hyperskill.app.progress_screen.domain.repository.ProgressesRepository
import org.hyperskill.app.progress_screen.remote.ProgressesRemoteDataSourceImpl

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