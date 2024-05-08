package org.hyperskill.app.analytic.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository

internal class AnalyticHyperskillRepositoryImpl(
    private val mutex: Mutex,
    private val hyperskillRemoteDataSource: AnalyticHyperskillRemoteDataSource,
    private val hyperskillCacheDataSource: AnalyticHyperskillCacheDataSource
) : AnalyticHyperskillRepository {
    override suspend fun logEvent(event: AnalyticEvent) {
        mutex.withLock {
            hyperskillCacheDataSource.logEvent(event)
        }
    }

    override suspend fun flushEvents(isAuthorized: Boolean): Result<Unit> {
        val eventsToFlush: List<AnalyticEvent>
        mutex.withLock {
            eventsToFlush = hyperskillCacheDataSource.getEvents()
            hyperskillCacheDataSource.clearEvents()
        }
        return hyperskillRemoteDataSource.flushEvents(eventsToFlush, isAuthorized)
    }
}