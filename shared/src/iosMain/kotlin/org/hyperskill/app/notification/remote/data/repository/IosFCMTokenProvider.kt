package org.hyperskill.app.notification.remote.data.repository

interface IosFCMTokenProvider {
    suspend fun getFCMToken(): String?
}

