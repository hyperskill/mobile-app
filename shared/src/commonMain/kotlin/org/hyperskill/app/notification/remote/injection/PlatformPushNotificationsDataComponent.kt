package org.hyperskill.app.notification.remote.injection

import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository

interface PlatformPushNotificationsDataComponent {
    val fcmTokenRepository: FCMTokenRepository
}