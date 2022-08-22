package org.hyperskill.app.analytic.data.source

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

interface AnalyticHyperskillCacheDataSource {
    suspend fun logEvent(event: AnalyticEvent)
    suspend fun getEvents(): List<AnalyticEvent>
    suspend fun clearEvents()
}