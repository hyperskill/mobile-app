package org.hyperskill.app.notification.remote.data.repository

import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository

class IosFCMTokenRepository(
    private val iosFCMTokenProvider: IosFCMTokenProvider
) : FCMTokenRepository {

    override suspend fun getToken(): Result<String?> =
        runCatching {
            iosFCMTokenProvider.getFCMToken()
        }
}