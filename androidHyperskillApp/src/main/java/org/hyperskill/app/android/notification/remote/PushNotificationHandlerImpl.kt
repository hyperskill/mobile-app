package org.hyperskill.app.android.notification.remote

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Looper
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.android.notification.NotificationBuilder
import org.hyperskill.app.android.notification.NotificationIntentBuilder
import org.hyperskill.app.android.notification.local.HyperskillNotificationManager
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.model.PushNotificationClickedData
import org.hyperskill.app.android.notification.remote.model.PushNotification
import org.hyperskill.app.android.notification.remote.model.channel
import org.hyperskill.app.android.notification.remote.model.id
import org.hyperskill.app.notification.remote.domain.analytic.PushNotificationShownHyperskillAnalyticEvent
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

class PushNotificationHandlerImpl(
    private val notificationManager: HyperskillNotificationManager,
    private val analyticInteractor: AnalyticInteractor,
    private val imageLoader: ImageLoader,
    private val sentryInteractor: SentryInteractor
) : PushNotificationHandler {
    override suspend fun onNotificationReceived(
        context: Context,
        notification: PushNotification?,
        data: PushNotificationData?
    ) {
        Log.d("PushNotificationHandlerImpl", "\nnotification=$notification\ndata=$data")
        if (data != null) {
            analyticInteractor.logEvent(
                PushNotificationShownHyperskillAnalyticEvent(data)
            )
        }
        if (notification != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                throw RuntimeException("Can't create notification on main thread")
            }
            val notificationId = data?.id?.notificationId ?: 0
            val androidNotification = NotificationBuilder.getSimpleNotificationBuilder(
                context = context,
                channel = data?.channel?.channelId ?: HyperskillNotificationChannel.Other.channelId,
                title = notification.title,
                body = notification.body,
                pendingIntent = with(NotificationIntentBuilder) {
                    buildActivityPendingIntent(context, notificationId.toInt()) {
                        if (data != null) {
                            addClickedNotificationDataExtra(PushNotificationClickedData(data))
                        }
                    }
                }
            ).apply {
                if (notification.image != null) {
                    getNotificationImage(context, notification.image) { bitmap ->
                        setLargeIcon(bitmap)
                    }
                }
            }
            notificationManager.showNotification(
                id = data?.id?.notificationId ?: 0,
                notification = androidNotification.build()
            )
        }
    }

    private suspend fun getNotificationImage(
        context: Context,
        imageSource: String,
        action: (Bitmap?) -> Unit
    ) {
        val request = ImageRequest.Builder(context)
            .data(imageSource)
            .listener(
                onSuccess = { _, successResult ->
                    action((successResult.drawable as BitmapDrawable).bitmap)
                },
                onError = { _, errorResult ->
                    action(null)
                    sentryInteractor.captureException(errorResult.throwable)
                }
            )
            .build()
        imageLoader.execute(request)
    }
}