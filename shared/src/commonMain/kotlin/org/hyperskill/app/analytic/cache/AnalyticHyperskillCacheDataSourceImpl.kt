package org.hyperskill.app.analytic.cache

import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.domain.model.AnalyticEvent

class AnalyticHyperskillCacheDataSourceImpl : AnalyticHyperskillCacheDataSource {
    private val events = mutableListOf<AnalyticEvent>()

    override suspend fun logEvent(event: AnalyticEvent) {
        events.add(event)
    }

    override suspend fun getEvents(): List<AnalyticEvent> =
        events.toList()

    override suspend fun clearEvents() =
        events.clear()
}