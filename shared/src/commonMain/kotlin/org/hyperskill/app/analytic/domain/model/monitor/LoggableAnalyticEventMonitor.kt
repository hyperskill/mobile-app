package org.hyperskill.app.analytic.domain.model.monitor

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.model.AnalyticEvent

/**
 * Log reported event to the [logger]
 */
internal class LoggableAnalyticEventMonitor(
    private val logger: Logger
) : AnalyticEventMonitor {
    override fun analyticDidReportEvent(event: AnalyticEvent) {
        logger.d { "Report event\neventName=${event.name}\nparams=${event.params}" }
    }

    override fun analyticDidFlushEvents() {
        // no op
    }
}