package org.hyperskill.app.analytic.data.repository

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository

class AnalyticHyperskillRepositoryImpl(
    private val mutex: Mutex,
    private val hyperskillRemoteDataSource: AnalyticHyperskillRemoteDataSource,
    private val hyperskillCacheDataSource: AnalyticHyperskillCacheDataSource
) : AnalyticHyperskillRepository {
    override suspend fun logEvent(event: AnalyticEvent) {
        mutex.withLock {
            hyperskillCacheDataSource.logEvent(event)
        }
    }

    override suspend fun flushEvents() {
        val eventsToFlush: List<AnalyticEvent>
        mutex.withLock {
            eventsToFlush = hyperskillCacheDataSource.getEvents()
            hyperskillCacheDataSource.clearEvents()
        }
        hyperskillRemoteDataSource.flushEvents(eventsToFlush)
    }
}