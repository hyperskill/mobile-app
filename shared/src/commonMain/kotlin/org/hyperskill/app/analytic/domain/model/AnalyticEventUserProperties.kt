package org.hyperskill.app.analytic.domain.model

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticKeys
import org.hyperskill.app.core.domain.model.ScreenOrientation
import ru.nobird.app.core.model.mapOfNotNull

data class AnalyticEventUserProperties(
    val userId: Long?,
    val isNotificationsPermissionGranted: Boolean,
    val isATTPermissionGranted: Boolean,
    val isInternalTesting: Boolean,
    val screenOrientation: ScreenOrientation,
    val platform: String
)

/**
 * Create a Map<String, Any> from [AnalyticEventUserProperties].
 * [AnalyticEventUserProperties.userId] is not presented to the result map.
 */
fun AnalyticEventUserProperties.asMapWithoutUserId(): Map<String, Any> =
    mapOfNotNull(
        HyperskillAnalyticKeys.PARAM_PLATFORM to platform,
        HyperskillAnalyticKeys.PARAM_IS_NOTIFICATIONS_ALLOW to isNotificationsPermissionGranted,
        HyperskillAnalyticKeys.PARAM_IS_ATT_ALLOW to isATTPermissionGranted,
        HyperskillAnalyticKeys.PARAM_SCREEN_ORIENTATION to when (screenOrientation) {
            ScreenOrientation.PORTRAIT -> HyperskillAnalyticKeys.SCREEN_ORIENTATION_VALUE_PORTRAIT
            ScreenOrientation.LANDSCAPE -> HyperskillAnalyticKeys.SCREEN_ORIENTATION_VALUE_LANDSCAPE
        },
        HyperskillAnalyticKeys.PARAM_IS_INTERNAL_TESTING to isInternalTesting,
    )
