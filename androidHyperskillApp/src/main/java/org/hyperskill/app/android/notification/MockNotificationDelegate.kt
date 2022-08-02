package org.hyperskill.app.android.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import org.hyperskill.app.android.R
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel

// TODO remove before merging to develop
class MockNotificationDelegate(
    private val context: Context,
    hyperskillNotificationManager: HyperskillNotificationManager
) : NotificationDelegate("mock_notification", hyperskillNotificationManager) {
    override fun onNeedShowNotification() {
        val notification = NotificationCompat.Builder(context, HyperskillNotificationChannel.other.channelId)
            .setContentTitle("Mock title")
            .setContentText("Mock content")
            .setSmallIcon(R.drawable.ic_close)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        showNotification(0, notification.build())
    }

    fun scheduleNotification() {
        val nowPlusMinute = ((System.currentTimeMillis() + 15000) / 1000)
        scheduleNotificationAt(nowPlusMinute)
    }
}