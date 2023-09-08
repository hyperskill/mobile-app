package org.hyperskill.app.notification.remote.injection

import org.hyperskill.app.notification.remote.data.repository.IosFCMTokenProvider
import org.hyperskill.app.notification.remote.data.repository.IosFCMTokenRepository
import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository

class IosPlatformPushNotificationsDataComponent(
    private val iosFCMTokenProvider: IosFCMTokenProvider
) : PlatformPushNotificationsDataComponent {
    override val fcmTokenRepository: FCMTokenRepository
        get() = IosFCMTokenRepository(iosFCMTokenProvider)
}