package org.hyperskill.app.android.notification.model

import kotlinx.serialization.Serializable
import org.hyperskill.app.notification.data.model.NotificationDescription

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