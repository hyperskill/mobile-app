package org.hyperskill.app.analytic.domain.amplitude

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.common.Logger
import com.amplitude.core.events.BaseEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEngine
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.processor.AmplitudeAnalyticEventMapper
import org.hyperskill.app.config.BuildKonfig

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
        amplitude.logger.logMode = if (isDebugMode) Logger.LogMode.DEBUG else Logger.LogMode.OFF
    }

    override suspend fun reportEvent(event: AnalyticEvent, force: Boolean) {
        if (event is HyperskillAnalyticEvent) {
            val amplitudeAnalyticEvent = AmplitudeAnalyticEventMapper.map(event)
            amplitude.track(
                BaseEvent().apply {
                    eventType = amplitudeAnalyticEvent.name
                    eventProperties = amplitudeAnalyticEvent.params.toMutableMap()
                    userProperties = this@AndroidAmplitudeAnalyticEngine.userProperties.toMutableMap()
                }
            )
        }
    }
}