package org.hyperskill.app.notification.remote.domain.analytic

import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import ru.nobird.app.core.model.mapOfNotNull

internal object PushNotificationDataAnalyticContextMapper {
    fun map(pushNotificationData: PushNotificationData): Map<String, Any> =
        mapOfNotNull(
            PushNotificationHyperskillAnalyticParams.PARAM_TYPE to pushNotificationData.typeString,
            PushNotificationHyperskillAnalyticParams.PARAM_CATEGORY to pushNotificationData.categoryString,
            PushNotificationHyperskillAnalyticParams.PARAM_IMAGE to pushNotificationData.image,
            PushNotificationHyperskillAnalyticParams.PARAM_NOTIFICATION_ID to pushNotificationData.notificationId
        )
}