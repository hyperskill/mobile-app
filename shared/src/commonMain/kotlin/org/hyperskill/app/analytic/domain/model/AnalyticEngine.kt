package org.hyperskill.app.analytic.domain.model

import org.hyperskill.app.core.domain.model.ScreenOrientation

interface AnalyticEngine {
    val targetSource: AnalyticSource

    suspend fun reportEvent(event: AnalyticEvent, force: Boolean = false)
    suspend fun flushEvents()

    fun setScreenOrientation(screenOrientation: ScreenOrientation)
    fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean)
}