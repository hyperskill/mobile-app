package org.hyperskill.app.analytic.domain.model.monitor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

/**
 * Interface outlining the lifetime events inside Analytic.
 */
interface AnalyticEventMonitor {
    fun analyticDidReportEvent(event: AnalyticEvent)
    fun analyticDidFlushEvents()
}