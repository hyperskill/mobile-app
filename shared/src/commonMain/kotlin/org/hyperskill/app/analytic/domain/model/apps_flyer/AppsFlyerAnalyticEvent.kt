package org.hyperskill.app.analytic.domain.model.apps_flyer

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticSource

/**
 * Represents a base analytic event that should be reported to AppsFlyer.
 *
 * @property name The name of the event.
 * @property params The parameters of the event.
 *
 * @see AnalyticEvent
 */
open class AppsFlyerAnalyticEvent(
    override val name: String,
    override val params: Map<String, Any> = emptyMap()
) : AnalyticEvent {
    override val sources: Set<AnalyticSource> = setOf(AnalyticSource.APPS_FLYER)
}