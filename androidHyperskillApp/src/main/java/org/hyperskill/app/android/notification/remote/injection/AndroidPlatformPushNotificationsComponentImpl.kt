package org.hyperskill.app.android.notification.remote.injection

import com.google.firebase.messaging.FirebaseMessaging
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.android.notification.local.injection.PlatformLocalNotificationComponent
import org.hyperskill.app.android.notification.remote.FcmNotificationParser
import org.hyperskill.app.android.notification.remote.PushNotificationHandler
import org.hyperskill.app.android.notification.remote.PushNotificationHandlerImpl
import org.hyperskill.app.android.notification.remote.domain.FCMTokenProviderImpl
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.notification.remote.injection.PushNotificationsComponent
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent
import org.hyperskill.app.push_notifications.domain.FCMTokenProvider
import org.hyperskill.app.push_notifications.domain.PushNotificationDeviceRegistrar

class AndroidPlatformPushNotificationsComponentImpl(
    private val pushNotificationsComponent: PushNotificationsComponent,
    private val playServicesCheckerComponent: PlayServicesCheckerComponent,
    private val commonComponent: CommonComponent,
    private val platformLocalNotificationComponent: PlatformLocalNotificationComponent,
    private val analyticInteractor: AnalyticInteractor
) : AndroidPlatformPushNotificationComponent {
    private val fcmTokenProvider: FCMTokenProvider
        get() = FCMTokenProviderImpl(FirebaseMessaging.getInstance())

    override val pushNotificationDeviceRegistrar: PushNotificationDeviceRegistrar
        get() = PushNotificationDeviceRegistrar(
            fcmTokenProvider = fcmTokenProvider,
            pushNotificationsInteractor = pushNotificationsComponent.pushNotificationsInteractor,
            playServicesChecker = playServicesCheckerComponent.playServicesChecker
        )

    override val fcmNotificationParser: FcmNotificationParser
        get() = FcmNotificationParser(commonComponent.json)

    override val pushNotificationHandler: PushNotificationHandler
        get() = PushNotificationHandlerImpl(
            platformLocalNotificationComponent.notificationManager,
            analyticInteractor
        )
}