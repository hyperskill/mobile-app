package org.hyperskill.app.analytic.domain.model.amplitude

import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticSource
import org.hyperskill.app.analytic.domain.model.analyticValue
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.model.ScreenOrientation
import ru.nobird.app.core.model.mapOfNotNull

abstract class AmplitudeAnalyticEngine : AnalyticEngine {

    private var screenOrientation: ScreenOrientation? = null
    private var isATTPermissionGranted: Boolean = false
    private val isInternalTesting: Boolean
        get() = BuildKonfig.IS_INTERNAL_TESTING ?: false

    final override val targetSource: AnalyticSource
        get() = AnalyticSource.AMPLITUDE

    final override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        this.isATTPermissionGranted = isAuthorized
    }

    final override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        this.screenOrientation = screenOrientation
    }

    final override suspend fun flushEvents() {
        // no op
    }
}