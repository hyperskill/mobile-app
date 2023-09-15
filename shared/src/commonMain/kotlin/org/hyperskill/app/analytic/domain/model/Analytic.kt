package org.hyperskill.app.analytic.domain.model

import org.hyperskill.app.core.domain.model.ScreenOrientation

interface Analytic {
    val eventMonitor: AnalyticEventMonitor?

    fun reportEvent(event: AnalyticEvent, forceReportEvent: Boolean = false)

    suspend fun flushEvents()

    fun setScreenOrientation(screenOrientation: ScreenOrientation)
}