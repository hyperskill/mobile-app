package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.AnalyticEventUserProperties
import org.hyperskill.app.analytic.domain.model.asMapWithoutUserId
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillProcessedAnalyticEvent
import ru.nobird.app.core.model.safeCast

internal object AnalyticHyperskillEventProcessor {

    fun processEvent(
        event: AnalyticEvent,
        userId: Long,
        userProperties: AnalyticEventUserProperties
    ): HyperskillProcessedAnalyticEvent {
        val resultParams = event.params.toMutableMap()

        resultParams[HyperskillAnalyticKeys.PARAM_CONTEXT] = getContextMap(event.params, userProperties)
        resultParams[HyperskillAnalyticKeys.PARAM_USER] = userId

        return HyperskillProcessedAnalyticEvent(name = event.name, params = resultParams.toMap())
    }

    private fun getContextMap(
        params: Map<String, Any>,
        userProperties: AnalyticEventUserProperties
    ): Map<String, Any> {
        val userPropertiesMap = userProperties.asMapWithoutUserId()
        val currentContextMap = params[HyperskillAnalyticKeys.PARAM_CONTEXT]?.safeCast<Map<String, Any>>()
        return if (currentContextMap == null) {
            userPropertiesMap
        } else {
            currentContextMap + userPropertiesMap
        }
    }
}