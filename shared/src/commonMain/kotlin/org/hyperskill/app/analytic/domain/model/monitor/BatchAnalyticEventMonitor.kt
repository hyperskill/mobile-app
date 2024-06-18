package org.hyperskill.app.analytic.domain.model.monitor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent

/**
 * A delegate that forwards the events to each of [monitors].
 *
 * @property monitors a list of [AnalyticEventMonitor] instances to monitor events
 */
internal class BatchAnalyticEventMonitor(
    private val monitors: List<AnalyticEventMonitor>
) : AnalyticEventMonitor {
    override fun analyticDidReportEvent(event: AnalyticEvent) {
        monitors.forEach {
            it.analyticDidReportEvent(event)
        }
    }

    override fun analyticDidFlushEvents() {
        monitors.forEach {
            it.analyticDidFlushEvents()
        }
    }
}