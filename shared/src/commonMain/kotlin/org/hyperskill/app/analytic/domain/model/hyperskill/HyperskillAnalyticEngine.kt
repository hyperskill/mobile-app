package org.hyperskill.app.analytic.domain.model.hyperskill

import org.hyperskill.app.analytic.domain.model.AnalyticEngine
import org.hyperskill.app.analytic.domain.model.AnalyticSource

abstract class HyperskillAnalyticEngine : AnalyticEngine {
    final override val targetSource: AnalyticSource
        get() = AnalyticSource.HYPERSKILL_API
}