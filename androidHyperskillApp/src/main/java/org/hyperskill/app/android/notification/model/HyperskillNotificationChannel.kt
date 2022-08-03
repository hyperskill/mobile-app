package org.hyperskill.app.android.notification.model

import android.app.NotificationManager
import androidx.annotation.StringRes
import org.hyperskill.app.android.R

private fun getImportanceCompat(): Int =
    NotificationManager.IMPORTANCE_HIGH

private val commentId = "commentChannel"
private val reviewId = "reviewChannel"
private val teachId = "teachChannel"
private val learnId = "learnChannel"
private val otherId = "otherChannel"
private val userId = "userChannel"

enum class HyperskillNotificationChannel(
    val channelId: String,
    @StringRes
    val visibleChannelNameRes: Int,
    @StringRes
    val visibleChannelDescriptionRes: Int,
    val importance: Int = getImportanceCompat()
) {
    // order is important!

//    comments(commentId, R.string.comments_channel_name, R.string.comments_channel_description),
//    review(reviewId, R.string.review_channel_name, R.string.review_channel_description),
//    teach(teachId, R.string.teach_channel_name, R.string.teach_channel_description),
//    learn(learnId, R.string.learn_channel_name, R.string.learn_channel_description),
    other(otherId, R.string.other_channel_name, R.string.other_channel_name),
//    user(userId, R.string.user_channel_name, R.string.user_channel_description)
}
