package org.hyperskill.app.android.notification.remote.injection

import org.hyperskill.app.android.notification.remote.FcmNotificationParser
import org.hyperskill.app.android.notification.remote.PushNotificationHandler

interface AndroidPlatformPushNotificationComponent {
    val fcmNotificationParser: FcmNotificationParser
    val pushNotificationHandler: PushNotificationHandler
}