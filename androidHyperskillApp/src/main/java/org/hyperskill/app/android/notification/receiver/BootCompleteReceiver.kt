package org.hyperskill.app.android.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.hyperskill.app.android.notification.service.BootCompleteService

@Deprecated("Replace with WorkManager")
class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            BootCompleteService.enqueueWork(context, intent)
        }
    }
}