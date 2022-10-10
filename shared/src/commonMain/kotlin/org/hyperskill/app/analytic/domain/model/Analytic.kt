package org.hyperskill.app.analytic.domain.model

interface Analytic {
    fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean = false)
    suspend fun flushEvents()
}