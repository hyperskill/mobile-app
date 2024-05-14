package org.hyperskill.app.sentry.domain.model.breadcrumb

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys
import org.hyperskill.app.sentry.domain.model.level.HyperskillSentryLevel

object HyperskillSentryBreadcrumbAnalyticEventMapper {
    private val PARAMS_TO_IGNORE = setOf(
        HyperskillAnalyticKeys.PARAM_CLIENT_TIME,
        HyperskillAnalyticKeys.PARAM_PLATFORM
    )

    fun mapAnalyticEvent(event: AnalyticEvent): HyperskillSentryBreadcrumb =
        HyperskillSentryBreadcrumb(
            category = HyperskillSentryBreadcrumbCategory.ANALYTIC_EVENT,
            message = event.name,
            level = HyperskillSentryLevel.INFO,
            data = event.params.filter { PARAMS_TO_IGNORE.contains(it.key).not() }
        )
}