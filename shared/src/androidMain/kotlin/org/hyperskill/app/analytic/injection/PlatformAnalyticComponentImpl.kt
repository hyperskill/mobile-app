package org.hyperskill.app.analytic.injection

import android.content.Context
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.analytic.domain.amplitude.AndroidAmplitudeAnalyticEngine
import org.hyperskill.app.analytic.domain.apps_flyer.AndroidAppsFlyerAnalyticEngine
import org.hyperskill.app.logging.injection.LoggerComponent

internal class PlatformAnalyticComponentImpl(
    loggerComponent: LoggerComponent,
    applicationContext: Context
) : PlatformAnalyticComponent {
    override val appsFlyerAnalyticEngine: AndroidAppsFlyerAnalyticEngine by lazy {
        AndroidAppsFlyerAnalyticEngine(
            logger = loggerComponent.logger,
            applicationContext = applicationContext,
            isDebugMode = BuildConfig.DEBUG
        )
    }
    override val amplitudeAnalyticEngine: AndroidAmplitudeAnalyticEngine by lazy {
        AndroidAmplitudeAnalyticEngine(
            isDebugMode = BuildConfig.DEBUG
        )
    }
}