package org.hyperskill.app.android.notification.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.notification.NotificationPublisher

class NotificationAlarmService : JobIntentService() {
    private lateinit var notificationPublisher: NotificationPublisher

    companion object {
        private const val JOB_ID = 1100

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NotificationAlarmService::class.java, JOB_ID, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationPublisher = HyperskillApp.graph().platformNotificationComponent.notificationPublisher
    }

    override fun onHandleWork(intent: Intent) {
        val action = intent.action ?: return
        notificationPublisher.onNeedShowNotificationWithId(action)
    }
}