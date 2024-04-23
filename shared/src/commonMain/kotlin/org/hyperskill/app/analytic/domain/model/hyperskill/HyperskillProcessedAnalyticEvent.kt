package org.hyperskill.app.analytic.domain.model.hyperskill

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource

class HyperskillProcessedAnalyticEvent(
    override val name: String,
    override val params: Map<String, Any>
) : AnalyticEvent {
    override val sources: Set<AnalyticSource> = setOf(AnalyticSource.HYPERSKILL_API)
}