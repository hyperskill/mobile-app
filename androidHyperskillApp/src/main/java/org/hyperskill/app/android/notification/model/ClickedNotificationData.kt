package org.hyperskill.app.android.notification.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.notification.local.data.model.NotificationDescription
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData

/**
 * Represent an information about notification witch was clicked by a user
 * */
@Serializable
sealed interface ClickedNotificationData

@Serializable
data class DefaultNotificationClickedData(
    val channel: HyperskillNotificationChannel
) : ClickedNotificationData

/**
 * @param notificationId represent an id of random DailyStudyReminderNotification
 * @see [NotificationDescription]
 * */
@Serializable
data class DailyStudyReminderClickedData(
    val notificationId: Int
) : ClickedNotificationData

@Serializable
data class PushNotificationClickedData(
    val data: PushNotificationData
) : ClickedNotificationData