package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.domain.apps_flyer.AndroidAppsFlyerAnalyticEngine
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEngine

interface PlatformAnalyticComponent {
    val appsFlyerAnalyticEngine: AndroidAppsFlyerAnalyticEngine
    val amplitudeAnalyticEngine: AmplitudeAnalyticEngine
}