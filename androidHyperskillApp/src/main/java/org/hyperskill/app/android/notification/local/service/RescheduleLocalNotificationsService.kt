package org.hyperskill.app.android.notification.local.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.notification.local.LocalNotificationDelegate

class RescheduleLocalNotificationsService : JobIntentService() {
    private lateinit var localNotificationDelegates: Set<@JvmSuppressWildcards LocalNotificationDelegate>

    companion object {
        private const val JOB_ID = 1000

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, RescheduleLocalNotificationsService::class.java, JOB_ID, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        localNotificationDelegates = HyperskillApp.graph().platformLocalNotificationComponent.getNotificationDelegates()
    }

    override fun onHandleWork(intent: Intent) {
        localNotificationDelegates.forEach { it.rescheduleNotification() }
    }
}