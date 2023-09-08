package org.hyperskill.app.notification.remote.domain.repository

interface FCMTokenRepository {
    suspend fun getToken(): Result<String?>
}