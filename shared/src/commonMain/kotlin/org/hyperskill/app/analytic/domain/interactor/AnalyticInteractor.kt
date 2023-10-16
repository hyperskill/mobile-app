package org.hyperskill.app.analytic.domain.interactor

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventMonitor
import org.hyperskill.app.core.domain.model.ScreenOrientation

class AnalyticInteractor(
    private val analyticEngines: List<AnalyticEngine>,
    override val eventMonitor: AnalyticEventMonitor?
) : Analytic {
    override fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean) {
        MainScope().launch {
            logEvent(event, forceReportEvent)
        }
    }

    override suspend fun flushEvents() {
        analyticEngines.forEach { it.flushEvents() }
        eventMonitor?.analyticDidFlushEvents()
    }

    suspend fun logEvent(event: AnalyticEvent, forceLogEvent: Boolean = false) {
        analyticEngines
            .filter { it.targetSource == event.source }
            .forEach { it.reportEvent(event, forceLogEvent) }
        eventMonitor?.analyticDidReportEvent(event)
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        analyticEngines.forEach { it.setScreenOrientation(screenOrientation) }
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        analyticEngines.forEach { it.setAppTrackingTransparencyAuthorizationStatus(isAuthorized) }
    }
}