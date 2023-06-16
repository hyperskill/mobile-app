package org.hyperskill.app.android.push_notifications.injection

import com.google.firebase.messaging.FirebaseMessaging
import org.hyperskill.app.android.push_notifications.domain.FCMTokenProviderImpl
import org.hyperskill.app.notification.remote.injection.PushNotificationsComponent
import org.hyperskill.app.play_services.injection.PlayServicesCheckerComponent
import org.hyperskill.app.push_notifications.domain.FCMTokenProvider
import org.hyperskill.app.push_notifications.domain.PushNotificationDeviceRegistrar
import org.hyperskill.app.push_notifications.injection.PlatformPushNotificationsComponent

class PlatformPushNotificationsComponentImpl(
    private val pushNotificationsComponent: PushNotificationsComponent,
    private val playServicesCheckerComponent: PlayServicesCheckerComponent
) : PlatformPushNotificationsComponent {
    private val fcmTokenProvider: FCMTokenProvider
        get() = FCMTokenProviderImpl(FirebaseMessaging.getInstance())

    override val pushNotificationDeviceRegistrar: PushNotificationDeviceRegistrar
        get() = PushNotificationDeviceRegistrar(
            fcmTokenProvider = fcmTokenProvider,
            pushNotificationsInteractor = pushNotificationsComponent.pushNotificationsInteractor,
            playServicesChecker = playServicesCheckerComponent.playServicesChecker
        )
}