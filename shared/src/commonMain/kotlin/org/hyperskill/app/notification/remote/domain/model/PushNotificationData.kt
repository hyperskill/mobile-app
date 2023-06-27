package org.hyperskill.app.notification.remote.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushNotificationData(
    @SerialName("category")
    val typeString: String,
    @SerialName("thread-id")
    val categoryString: String
) {
    val typeEnum: PushNotificationType =
        try {
            PushNotificationType.valueOf(typeString)
        } catch (e: Exception) {
            PushNotificationType.UNKNOWN
        }

    val categoryEnum: PushNotificationCategory =
        try {
            PushNotificationCategory.getByBackendName(categoryString)
        } catch (e: Exception) {
            PushNotificationCategory.UNKNOWN
        }
}