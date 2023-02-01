package org.hyperskill.app.analytic.domain.model

interface Analytic {
    val eventMonitor: AnalyticEventMonitor?

    fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean = false)

    suspend fun flushEvents()
}