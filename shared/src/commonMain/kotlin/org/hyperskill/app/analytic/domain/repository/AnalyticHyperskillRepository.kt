package org.hyperskill.app.analytic.domain.repository

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

interface AnalyticHyperskillRepository {
    suspend fun logEvent(event: AnalyticEvent)
    suspend fun flushEvents()
}