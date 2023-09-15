package org.hyperskill.app.analytic.domain.processor

import org.hyperskill.app.analytic.domain.model.AnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillProcessedAnalyticEvent
import org.hyperskill.app.core.domain.model.ScreenOrientation
import org.hyperskill.app.core.domain.platform.Platform
import ru.nobird.app.core.model.mapOfNotNull
import ru.nobird.app.core.model.safeCast

class AnalyticHyperskillEventProcessor(
    private val platform: Platform
) {
    companion object {
        private const val PARAM_CONTEXT = "context"
        private const val PARAM_PLATFORM = "platform"
        private const val PARAM_USER = "user"
        private const val PARAM_IS_NOTIFICATIONS_ALLOW = "is_notifications_allow"
        private const val PARAM_SCREEN_ORIENTATION = "screen_orientation"
        private const val SCREEN_ORIENTATION_VALUE_PORTRAIT = "portrait"
        private const val SCREEN_ORIENTATION_VALUE_LANDSCAPE = "landscape"
    }

    fun processEvent(
        event: AnalyticEvent,
        userId: Long,
        isNotificationsPermissionGranted: Boolean,
        screenOrientation: ScreenOrientation
    ): HyperskillProcessedAnalyticEvent {
        val resultParams = event.params.toMutableMap()

        if (resultParams.containsKey(PARAM_CONTEXT)) {
            val contextMap = resultParams[PARAM_CONTEXT]
                .safeCast<Map<String, Any>>()
                ?.toMutableMap()

            if (contextMap != null) {
                contextMap[PARAM_PLATFORM] = platform.analyticName
                contextMap[PARAM_IS_NOTIFICATIONS_ALLOW] = isNotificationsPermissionGranted
                contextMap[PARAM_SCREEN_ORIENTATION] = getScreenOrientationValue(screenOrientation)
                resultParams[PARAM_CONTEXT] = contextMap
            }
        } else {
            resultParams[PARAM_CONTEXT] = mapOfNotNull(
                PARAM_PLATFORM to platform.analyticName,
                PARAM_IS_NOTIFICATIONS_ALLOW to isNotificationsPermissionGranted,
                PARAM_SCREEN_ORIENTATION to getScreenOrientationValue(screenOrientation)
            )
        }

        resultParams[PARAM_USER] = userId

        return HyperskillProcessedAnalyticEvent(name = event.name, params = resultParams.toMap())
    }

    private fun getScreenOrientationValue(screenOrientation: ScreenOrientation): String =
        when (screenOrientation) {
            ScreenOrientation.PORTRAIT -> SCREEN_ORIENTATION_VALUE_PORTRAIT
            ScreenOrientation.LANDSCAPE -> SCREEN_ORIENTATION_VALUE_LANDSCAPE
        }
}