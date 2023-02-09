package org.hyperskill.app.android.notification.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.notification.NotificationDelegate

class RescheduleNotificationsService : JobIntentService() {
    private lateinit var notificationDelegates: Set<@JvmSuppressWildcards NotificationDelegate>

    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, RescheduleNotificationsService::class.java, JOB_ID, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationDelegates = HyperskillApp.graph().platformNotificationComponent.getNotificationDelegates()
    }

    override fun onHandleWork(intent: Intent) {
        notificationDelegates.forEach { it.rescheduleNotification() }
    }
}