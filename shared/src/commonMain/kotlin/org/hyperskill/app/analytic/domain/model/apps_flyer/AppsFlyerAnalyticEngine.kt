package org.hyperskill.app.analytic.domain.model.apps_flyer

import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticSource

abstract class AppsFlyerAnalyticEngine : AnalyticEngine {
    final override val targetSource: AnalyticSource
        get() = AnalyticSource.APPS_FLYER
}