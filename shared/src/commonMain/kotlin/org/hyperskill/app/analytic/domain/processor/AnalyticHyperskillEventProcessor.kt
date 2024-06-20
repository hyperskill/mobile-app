package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.analytic.domain.model.AnalyticKeys
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillProcessedAnalyticEvent
import ru.nobird.app.core.model.safeCast

internal object AnalyticHyperskillEventProcessor {

    fun processEvent(
        event: AnalyticEvent,
        userId: Long,
        userProperties: AnalyticEventUserProperties
    ): HyperskillProcessedAnalyticEvent {
        val resultParams = event.params.toMutableMap()

        resultParams[AnalyticKeys.PARAM_CONTEXT] = getContextMap(event.params, userProperties.properties)
        resultParams[AnalyticKeys.PARAM_USER] = userId

        return HyperskillProcessedAnalyticEvent(name = event.name, params = resultParams.toMap())
    }

    private fun getContextMap(
        params: Map<String, Any>,
        userProperties: Map<String, Any>
    ): Map<String, Any> {
        val currentContextMap = params[AnalyticKeys.PARAM_CONTEXT]?.safeCast<Map<String, Any>>()
        return if (currentContextMap == null) {
            userProperties
        } else {
            currentContextMap + userProperties
        }
    }
}