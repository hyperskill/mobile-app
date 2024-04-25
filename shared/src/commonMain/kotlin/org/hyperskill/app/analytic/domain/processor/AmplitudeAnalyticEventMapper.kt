package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys

object AmplitudeAnalyticEventMapper {
    fun map(analyticEvent: AnalyticEvent): AmplitudeAnalyticEvent? =
        when (analyticEvent) {
            is HyperskillAnalyticEvent -> AmplitudeAnalyticEvent(
                name = getType(analyticEvent),
                params = getParams(analyticEvent)
            )
            is AmplitudeAnalyticEvent -> analyticEvent
            else -> null
        }

    private fun getType(analyticEvent: HyperskillAnalyticEvent): String =
        buildString {
            append(analyticEvent.action.actionName)
            append(' ')
            if (analyticEvent.part != null) {
                append(analyticEvent.part.partName)
                append(' ')
            }
            if (analyticEvent.target != null) {
                append(analyticEvent.target.targetName)
            }
        }

    private fun getParams(analyticEvent: HyperskillAnalyticEvent): Map<String, Any> =
        buildMap {
            put(HyperskillAnalyticKeys.PARAM_ROUTE, analyticEvent.route.path)
            if (analyticEvent.context != null) {
                putAll(analyticEvent.context)
            }
        }
}