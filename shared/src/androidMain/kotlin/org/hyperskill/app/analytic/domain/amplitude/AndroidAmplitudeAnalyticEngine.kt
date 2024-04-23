package org.hyperskill.app.analytic.domain.amplitude

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEngine
import org.hyperskill.app.core.domain.model.ScreenOrientation

// TODO: ALTAPPS-1235
class AndroidAmplitudeAnalyticEngine : AmplitudeAnalyticEngine() {
    override suspend fun reportEvent(event: AnalyticEvent, force: Boolean) {
        TODO("TODO: ALTAPPS-1235")
    }

    override suspend fun flushEvents() {
        TODO("TODO: ALTAPPS-1235")
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        TODO("TODO: ALTAPPS-1235")
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        TODO("TODO: ALTAPPS-1235")
    }
}