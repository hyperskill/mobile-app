package org.hyperskill.app.android.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.hyperskill.app.android.notification.service.NotificationAlarmService

@Deprecated("Replace with WorkManager")
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val REQUEST_CODE = 151

        fun createIntent(context: Context, action: String): Intent =
            Intent(context, AlarmReceiver::class.java)
                .setAction(action)
    }

    override fun onReceive(context: Context, intent: Intent) {
        NotificationAlarmService.enqueueWork(context, intent)
    }
}