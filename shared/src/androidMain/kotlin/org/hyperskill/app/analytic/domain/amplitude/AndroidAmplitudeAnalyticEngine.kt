package org.hyperskill.app.analytic.domain.amplitude

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.common.Logger
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEngine
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.core.domain.model.ScreenOrientation

class AndroidAmplitudeAnalyticEngine(
    private val isDebugMode: Boolean
) : AmplitudeAnalyticEngine() {

    companion object {
        private lateinit var amplitude: Amplitude
    }

    fun startup(applicationContext: Context) {
        amplitude = Amplitude(
            Configuration(
                apiKey = BuildKonfig.AMPLITUDE_DEV_KEY,
                context = applicationContext
            )
        )
        amplitude.logger.logMode = if (isDebugMode) Logger.LogMode.INFO else Logger.LogMode.OFF
    }

    override suspend fun reportEvent(event: AnalyticEvent, force: Boolean) {
        TODO("TODO: ALTAPPS-1235")
    }

    override suspend fun flushEvents() {
        // no op
    }

    override fun setScreenOrientation(screenOrientation: ScreenOrientation) {
        // no op
    }

    override fun setAppTrackingTransparencyAuthorizationStatus(isAuthorized: Boolean) {
        // no op
    }
}