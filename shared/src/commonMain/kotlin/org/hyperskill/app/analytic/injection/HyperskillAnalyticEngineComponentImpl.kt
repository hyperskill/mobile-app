package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.cache.AnalyticHyperskillCacheDataSourceImpl
import org.hyperskill.app.analytic.data.repository.AnalyticHyperskillRepositoryImpl
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEngine
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEngineImpl
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.analytic.remote.AnalyticHyperskillRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

internal class HyperskillAnalyticEngineComponentImpl(appGraph: AppGraph) : HyperskillAnalyticEngineComponent {
    companion object {
        private const val LOG_TAG = "HyperskillAnalyticEngine"
    }

    private val hyperskillRemoteDataSource: AnalyticHyperskillRemoteDataSource =
        AnalyticHyperskillRemoteDataSourceImpl(
            appGraph.networkComponent.authorizedHttpClient,
            appGraph.networkComponent.frontendEventsUnauthorizedHttpClient
        )
    private val hyperskillCacheDataSource: AnalyticHyperskillCacheDataSource =
        AnalyticHyperskillCacheDataSourceImpl()
    private val hyperskillRepository: AnalyticHyperskillRepository =
        AnalyticHyperskillRepositoryImpl(
            hyperskillRemoteDataSource,
            hyperskillCacheDataSource
        )

    override val hyperskillAnalyticEngine: HyperskillAnalyticEngine =
        HyperskillAnalyticEngineImpl(
            authInteractor = appGraph.authComponent.authInteractor,
            analyticHyperskillRepository = hyperskillRepository,
            logger = appGraph.loggerComponent.logger.withTag(LOG_TAG)
        )
}