package org.hyperskill.app.analytic.domain.interactor

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.hyperskill.app.analytic.domain.model.Analytic
import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventMonitor
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEngine
import org.hyperskill.app.core.domain.model.ScreenOrientation

class AnalyticInteractor(
    analyticEngines: List<AnalyticEngine>,
    override val eventMonitor: AnalyticEventMonitor?
) : Analytic {
    private val hyperskillAnalyticEngine: AnalyticEngine =
        analyticEngines.first { it.targetSource == AnalyticSource.HYPERSKILL_API }

    private val appsFlyerAnalyticEngine: AnalyticEngine? =
        analyticEngines.firstOrNull { it.targetSource == AnalyticSource.APPS_FLYER }

    override fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean) {
        MainScope().launch {
            logEvent(event, forceReportEvent)
        }
    }

    override suspend fun flushEvents() {
        hyperskillAnalyticEngine.flushEvents()
        appsFlyerAnalyticEngine?.flushEvents()

        eventMonitor?.analyticDidFlushEvents()
    }

    suspend fun logEvent(event: AnalyticEvent, forceLogEvent: Boolean = false) {
        when (event.source) {
            AnalyticSource.HYPERSKILL_API -> {
                hyperskillAnalyticEngine.reportEvent(event, force = forceLogEvent)
            }
            AnalyticSource.APPS_FLYER -> {
                appsFlyerAnalyticEngine?.reportEvent(event, force = forceLogEvent)
            }
        }
        eventMonitor?.analyticDidReportEvent(event)
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        // TODO: Find a better way for providing ScreenOrientation
        if (hyperskillAnalyticEngine is HyperskillAnalyticEngine) {
            hyperskillAnalyticEngine.setScreenOrientation(screenOrientation)
        }
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        if (hyperskillAnalyticEngine is HyperskillAnalyticEngine) {
            hyperskillAnalyticEngine.setAppTrackingTransparencyAuthorizationStatus(isAuthorized = isAuthorized)
        }
    }
}