package org.hyperskill.app.android.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.hyperskill.app.android.notification.service.RescheduleNotificationsService

class RescheduleNotificationsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            RescheduleNotificationsService.enqueueWork(context, intent)
        }
    }
}