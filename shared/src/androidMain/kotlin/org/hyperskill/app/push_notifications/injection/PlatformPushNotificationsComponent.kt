package org.hyperskill.app.push_notifications.injection

import org.hyperskill.app.push_notifications.domain.PushNotificationDeviceRegistrar

interface PlatformPushNotificationsComponent {
    val pushNotificationDeviceRegistrar: PushNotificationDeviceRegistrar
}