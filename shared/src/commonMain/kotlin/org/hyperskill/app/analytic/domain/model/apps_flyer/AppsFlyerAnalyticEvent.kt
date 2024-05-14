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
    final override val name: String,
    final override val params: Map<String, Any> = emptyMap()
) : AnalyticEvent {
    final override val sources: Set<AnalyticSource>
        get() = setOf(AnalyticSource.APPS_FLYER)
}