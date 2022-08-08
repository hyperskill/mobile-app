package org.hyperskill.app.android.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.extensions.DateTimeHelper
import org.hyperskill.app.android.main.view.ui.activity.MainActivity
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel

// TODO remove before merging to develop
class MockNotificationDelegate(
    private val context: Context,
    hyperskillNotificationManager: HyperskillNotificationManager
) : NotificationDelegate("mock_notification", hyperskillNotificationManager) {
    override fun onNeedShowNotification() {
        val intent = Intent(HyperskillApp.getAppContext(), MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(HyperskillApp.getAppContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, HyperskillNotificationChannel.other.channelId)
            .setContentTitle("Hyperskill Mock Notification")
            .setContentText("Tap to open the app!")
            .setSmallIcon(R.drawable.ic_branded_logo_splash)
            .setContentIntent(pendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        showNotification(0, notification.build())
    }

    fun scheduleNotification() {
        val nowPlusX = DateTimeHelper.nowUtc() + 60 * 2 * 1000
        scheduleNotificationAt(nowPlusX)
    }
}