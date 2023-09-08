package org.hyperskill.app.android.notification.remote.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.android.notification.local.injection.PlatformLocalNotificationComponent
import org.hyperskill.app.android.notification.remote.FcmNotificationParser
import org.hyperskill.app.android.notification.remote.PushNotificationHandler
import org.hyperskill.app.android.notification.remote.PushNotificationHandlerImpl
import org.hyperskill.app.core.injection.CommonComponent

class AndroidPlatformPushNotificationsComponentImpl(
    private val commonComponent: CommonComponent,
    private val platformLocalNotificationComponent: PlatformLocalNotificationComponent,
    private val analyticInteractor: AnalyticInteractor
) : AndroidPlatformPushNotificationComponent {

    override val fcmNotificationParser: FcmNotificationParser
        get() = FcmNotificationParser(commonComponent.json)

    override val pushNotificationHandler: PushNotificationHandler
        get() = PushNotificationHandlerImpl(
            platformLocalNotificationComponent.notificationManager,
            analyticInteractor
        )
}