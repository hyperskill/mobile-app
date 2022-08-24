package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.Platform
import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillProcessedAnalyticEvent
import ru.nobird.app.core.model.safeCast

class AnalyticHyperskillEventProcessor(
    private val platform: Platform
) {
    companion object {
        private const val PARAM_CONTEXT = "context"
        private const val PARAM_PLATFORM = "platform"
        private const val PARAM_USER = "user"
    }

    fun processEvent(event: AnalyticEvent, userId: Long): HyperskillProcessedAnalyticEvent {
        val resultParams = event.params.toMutableMap()

        if (resultParams.containsKey(PARAM_CONTEXT)) {
            val contextMap = resultParams[PARAM_CONTEXT]
                .safeCast<Map<String, Any>>()
                ?.toMutableMap()
            if (contextMap != null) {
                contextMap[PARAM_PLATFORM] = platform.analyticName
                resultParams[PARAM_CONTEXT] = contextMap
            }
        } else {
            resultParams[PARAM_CONTEXT] = mapOf(PARAM_PLATFORM to platform.analyticName)
        }

        resultParams[PARAM_USER] = userId

        return HyperskillProcessedAnalyticEvent(name = event.name, params = resultParams.toMap())
    }
}