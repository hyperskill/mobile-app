package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.CommonAnalyticKeys
import org.hyperskill.app.analytic.domain.model.analyticValue
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillProcessedAnalyticEvent
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.core.domain.platform.Platform
import ru.nobird.app.core.model.safeCast

class AnalyticHyperskillEventProcessor(
    private val platform: Platform
) {

    fun processEvent(
        event: AnalyticEvent,
        userId: Long,
        isNotificationsPermissionGranted: Boolean,
        isATTPermissionGranted: Boolean,
        screenOrientation: ScreenOrientation,
        isInternalTesting: Boolean
    ): HyperskillProcessedAnalyticEvent {
        val resultParams = event.params.toMutableMap()

        val contextMap = when (resultParams.containsKey(PARAM_CONTEXT)) {
            true -> resultParams[PARAM_CONTEXT].safeCast<Map<String, Any>>()?.toMutableMap()
            false -> mutableMapOf()
        } ?: mutableMapOf()

        updateContextMap(
            contextMap,
            isNotificationsPermissionGranted = isNotificationsPermissionGranted,
            isATTPermissionGranted = isATTPermissionGranted,
            screenOrientation = screenOrientation,
            isInternalTesting = isInternalTesting
        )
        resultParams[PARAM_CONTEXT] = contextMap
        resultParams[PARAM_USER] = userId

        return HyperskillProcessedAnalyticEvent(name = event.name, params = resultParams.toMap())
    }

    private fun updateContextMap(
        contextMap: MutableMap<String, Any>,
        isNotificationsPermissionGranted: Boolean,
        isATTPermissionGranted: Boolean,
        screenOrientation: ScreenOrientation,
        isInternalTesting: Boolean
    ) {
        contextMap.apply {
            this[PARAM_PLATFORM] = platform.analyticName
            this[PARAM_IS_NOTIFICATIONS_ALLOW] = isNotificationsPermissionGranted
            this[CommonAnalyticKeys.PARAM_IS_ATT_ALLOW] = isATTPermissionGranted
            this[CommonAnalyticKeys.PARAM_SCREEN_ORIENTATION] = screenOrientation.analyticValue
            this[CommonAnalyticKeys.PARAM_IS_INTERNAL_TESTING] = isInternalTesting
        }
    }
}