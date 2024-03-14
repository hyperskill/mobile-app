package org.hyperskill.app.analytic.injection

import org.hyperskill.app.analytic.domain.apps_flyer.AndroidAppsFlyerAnalyticEngine

interface PlatformAnalyticComponent {
    val appsFlyerAnalyticEngine: AndroidAppsFlyerAnalyticEngine
}