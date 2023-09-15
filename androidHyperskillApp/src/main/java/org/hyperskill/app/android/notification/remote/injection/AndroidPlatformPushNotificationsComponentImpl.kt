package org.hyperskill.app.android.notification.remote.injection

import org.hyperskill.app.android.core.injection.AndroidAppComponent
import org.hyperskill.app.android.notification.remote.FcmNotificationParser
import org.hyperskill.app.android.notification.remote.PushNotificationHandler
import org.hyperskill.app.android.notification.remote.PushNotificationHandlerImpl

class AndroidPlatformPushNotificationsComponentImpl(
    private val appGraph: AndroidAppComponent
) : AndroidPlatformPushNotificationComponent {

    override val fcmNotificationParser: FcmNotificationParser
        get() = FcmNotificationParser(appGraph.commonComponent.json)

    override val pushNotificationHandler: PushNotificationHandler
        get() = PushNotificationHandlerImpl(
            notificationManager = appGraph.platformLocalNotificationComponent.notificationManager,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            imageLoader = appGraph.imageLoadingComponent.imageLoader,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )
}