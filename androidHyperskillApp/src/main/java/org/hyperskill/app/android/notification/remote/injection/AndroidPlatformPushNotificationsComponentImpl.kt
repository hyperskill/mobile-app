package org.hyperskill.app.android.notification.remote.injection

import com.google.firebase.messaging.FirebaseMessaging
import org.hyperskill.app.android.core.injection.AndroidAppComponent
import org.hyperskill.app.android.notification.remote.FcmNotificationParser
import org.hyperskill.app.android.notification.remote.PushNotificationHandler
import org.hyperskill.app.android.notification.remote.PushNotificationHandlerImpl
import org.hyperskill.app.android.notification.remote.domain.FCMTokenProviderImpl
import org.hyperskill.app.push_notifications.domain.FCMTokenProvider
import org.hyperskill.app.push_notifications.domain.PushNotificationDeviceRegistrar

class AndroidPlatformPushNotificationsComponentImpl(
    private val appGraph: AndroidAppComponent
) : AndroidPlatformPushNotificationComponent {
    private val fcmTokenProvider: FCMTokenProvider
        get() = FCMTokenProviderImpl(FirebaseMessaging.getInstance())

    override val pushNotificationDeviceRegistrar: PushNotificationDeviceRegistrar
        get() = PushNotificationDeviceRegistrar(
            fcmTokenProvider = fcmTokenProvider,
            pushNotificationsInteractor = appGraph.buildPushNotificationsComponent().pushNotificationsInteractor,
            playServicesChecker = appGraph.buildPlayServicesCheckerComponent().playServicesChecker
        )

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