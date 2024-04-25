package org.hyperskill.app.analytic.domain.model.amplitude

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource

open class AmplitudeAnalyticEvent(
    final override val name: String,
    final override val params: Map<String, Any> = emptyMap()
) : AnalyticEvent {
    final override val sources: Set<AnalyticSource>
        get() = setOf(AnalyticSource.AMPLITUDE)
}