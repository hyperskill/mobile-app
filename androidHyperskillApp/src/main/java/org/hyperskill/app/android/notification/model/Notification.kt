package org.hyperskill.app.android.notification.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Notification(
    val id: Long? = 0,
    @SerialName("is_unread")
    var isUnread: Boolean? = null,
    @SerialName("is_muted")
    val isMuted: Boolean? = null,
    @SerialName("is_favorite")
    val isFavourite: Boolean? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("type")
    val type: NotificationType = NotificationType.other,
    @SerialName("level")
    val level: String? = null,
    @SerialName("priority")
    val priority: String? = null,
    @SerialName("html_text")
    var htmlText: String? = null,
    @SerialName("action")
    val action: String? = null,

    var courseId: Long? = null,
    var userAvatarUrl: String? = null,
    var notificationText: CharSequence? = null,
    var dateGroup: Int = 0
)

@Serializable
enum class NotificationType(val channel: HyperskillNotificationChannel) {
//    @SerialName("comments")
//    comments(HyperskillNotificationChannel.comments),
//
//    @SerialName("review")
//    review(HyperskillNotificationChannel.review),
//
//    @SerialName("teach")
//    teach(HyperskillNotificationChannel.teach),
//
//    @SerialName("learn")
//    learn(HyperskillNotificationChannel.learn),

    @SerialName("default")
    other(HyperskillNotificationChannel.other)
}