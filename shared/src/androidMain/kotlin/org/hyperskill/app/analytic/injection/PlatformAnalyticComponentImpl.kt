package org.hyperskill.app.analytic.injection

import android.content.Context
import org.hyperskill.app.BuildConfig
import org.hyperskill.app.analytic.domain.apps_flyer.AndroidAppsFlyerAnalyticEngine
import org.hyperskill.app.logging.inject.LoggerComponent

class PlatformAnalyticComponentImpl(
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
}