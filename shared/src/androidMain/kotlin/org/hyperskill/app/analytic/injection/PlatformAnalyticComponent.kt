package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.domain.amplitude.AndroidAmplitudeAnalyticEngine
import org.hyperskill.app.analytic.domain.apps_flyer.AndroidAppsFlyerAnalyticEngine

interface PlatformAnalyticComponent {
    val appsFlyerAnalyticEngine: AndroidAppsFlyerAnalyticEngine
    val amplitudeAnalyticEngine: AndroidAmplitudeAnalyticEngine
}