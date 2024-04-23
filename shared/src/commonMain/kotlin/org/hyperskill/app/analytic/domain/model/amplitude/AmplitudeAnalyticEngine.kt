package org.hyperskill.app.analytic.domain.model.amplitude

import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticSource

abstract class AmplitudeAnalyticEngine : AnalyticEngine {
    final override val targetSource: AnalyticSource
        get() = AnalyticSource.AMPLITUDE
}