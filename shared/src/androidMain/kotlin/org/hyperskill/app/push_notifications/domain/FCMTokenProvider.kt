package org.hyperskill.app.push_notifications.domain

interface FCMTokenProvider {
    suspend fun getToken(): String
}