package org.hyperskill.app.notification.remote.data.repository

import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository

class IosFCMTokenRepository : FCMTokenRepository {

    override suspend fun getToken(): Result<String?> =
        runCatching {
            // TODO: Implement FCM token fetch
            null
        }
}