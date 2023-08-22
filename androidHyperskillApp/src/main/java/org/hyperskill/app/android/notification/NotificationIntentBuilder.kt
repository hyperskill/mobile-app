package org.hyperskill.app.android.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.chrynan.parcelable.core.getParcelableExtra
import com.chrynan.parcelable.core.putExtra
import org.hyperskill.app.android.main.view.ui.activity.MainActivity
import org.hyperskill.app.android.notification.model.ClickedNotificationData

object NotificationIntentBuilder {
    private const val NotificationClickedKey = "NotificationClickedKey"

    fun buildActivityPendingIntent(
        context: Context,
        requestCode: Int,
        block: Intent.() -> Unit = {}
    ): PendingIntent =
        PendingIntentCompat.getActivity(
            context,
            requestCode,
            Intent(context, MainActivity::class.java).apply(block),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    fun Intent.addClickedNotificationDataExtra(notificationClickedData: ClickedNotificationData) {
        putExtra(NotificationClickedKey, notificationClickedData, serializer = ClickedNotificationData.serializer())
    }

    fun Intent.getClickedNotificationData(): ClickedNotificationData? =
        getParcelableExtra(NotificationClickedKey, deserializer = ClickedNotificationData.serializer())
}