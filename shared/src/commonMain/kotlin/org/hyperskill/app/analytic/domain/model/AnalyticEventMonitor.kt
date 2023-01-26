package org.hyperskill.app.analytic.domain.model

/**
 * Interface outlining the lifetime events inside Analytic.
 */
interface AnalyticEventMonitor {
    fun analyticDidReportEvent(event: AnalyticEvent)
    fun analyticDidFlushEvents()
}