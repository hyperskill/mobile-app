package org.hyperskill.app.analytic.domain.amplitude

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.amplitude.common.Logger
import com.amplitude.core.events.BaseEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEngine
import org.hyperskill.app.analytic.domain.model.asMapWithoutUserId
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

    override suspend fun reportEvent(
        event: AnalyticEvent,
        userProperties: AnalyticEventUserProperties,
        force: Boolean
    ) {
        val amplitudeAnalyticEvent = AmplitudeAnalyticEventMapper.map(event)
        if (amplitudeAnalyticEvent != null) {
            amplitude.track(
                BaseEvent().apply {
                    eventType = amplitudeAnalyticEvent.name
                    eventProperties = amplitudeAnalyticEvent.params.toMutableMap()
                    this.userProperties = userProperties.asMapWithoutUserId().toMutableMap()
                    this.userId = userProperties.userId?.toString()
                }
            )
        }
    }
}