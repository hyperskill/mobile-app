package org.hyperskill.app.analytic.data.repository

import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository

class AnalyticHyperskillRepositoryImpl(
    private val hyperskillRemoteDataSource: AnalyticHyperskillRemoteDataSource,
    private val hyperskillCacheDataSource: AnalyticHyperskillCacheDataSource
) : AnalyticHyperskillRepository {
    override suspend fun logEvent(event: AnalyticEvent) =
        hyperskillCacheDataSource.logEvent(event)

    override suspend fun flushEvents() {
        val events = hyperskillCacheDataSource.getEvents()
        hyperskillCacheDataSource.clearEvents()
        hyperskillRemoteDataSource.flushEvents(events)
    }
}