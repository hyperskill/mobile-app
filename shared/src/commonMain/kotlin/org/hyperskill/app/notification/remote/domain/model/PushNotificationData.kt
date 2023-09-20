package org.hyperskill.app.notification.remote.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushNotificationData(
    @SerialName("notification_id")
    val notificationId: String? = null,
    @SerialName("category")
    val typeString: String,
    @SerialName("thread-id")
    val categoryString: String,
    @SerialName("badge")
    private val badgeIdString: String? = null,
    @SerialName("image")
    val image: String? = null
) {
    val typeEnum: PushNotificationType
        get() = try {
            PushNotificationType.valueOf(typeString)
        } catch (e: Exception) {
            PushNotificationType.UNKNOWN
        }

    val categoryEnum: PushNotificationCategory
        get() = try {
            PushNotificationCategory.getByBackendName(categoryString)
        } catch (e: Exception) {
            PushNotificationCategory.UNKNOWN
        }

    val badgeId: Long?
        get() = badgeIdString?.toLongOrNull()
}