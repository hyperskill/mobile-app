package org.hyperskill.app.analytic.injection

import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.analytic.cache.AnalyticHyperskillCacheDataSourceImpl
import org.hyperskill.app.analytic.data.repository.AnalyticHyperskillRepositoryImpl
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.analytic.remote.AnalyticHyperskillRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

class AnalyticComponentImpl(
    appGraph: AppGraph
) : AnalyticComponent {
    private val hyperskillRemoteDataSource: AnalyticHyperskillRemoteDataSource =
        AnalyticHyperskillRemoteDataSourceImpl(
            appGraph.networkComponent.authorizedHttpClient,
            appGraph.networkComponent.frontendEventsUnauthorizedHttpClient
        )
    private val hyperskillCacheDataSource: AnalyticHyperskillCacheDataSource =
        AnalyticHyperskillCacheDataSourceImpl()
    private val hyperskillRepository: AnalyticHyperskillRepository =
        AnalyticHyperskillRepositoryImpl(
            Mutex(),
            hyperskillRemoteDataSource,
            hyperskillCacheDataSource
        )

    private val hyperskillEventProcessor =
        AnalyticHyperskillEventProcessor(appGraph.commonComponent.platform)

    override val analyticInteractor: AnalyticInteractor =
        AnalyticInteractor(
            appGraph.authComponent.authInteractor,
            appGraph.buildProfileDataComponent().currentProfileStateRepository,
            appGraph.buildNotificationComponent().notificationInteractor,
            hyperskillRepository,
            hyperskillEventProcessor,
            appGraph.sentryComponent.sentryInteractor
        )
}