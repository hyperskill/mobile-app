package org.hyperskill.app.android.notification.model

@kotlinx.serialization.Serializable
data class ClickedNotificationData(
    val notificationId: Long,
    val notificationChannel: HyperskillNotificationChannel
)
