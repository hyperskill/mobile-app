package org.hyperskill.app.android.notification.remote.domain

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import org.hyperskill.app.push_notifications.domain.FCMTokenProvider

class FCMTokenProviderImpl(
    private val firebaseMessaging: FirebaseMessaging
) : FCMTokenProvider {
    override suspend fun getToken(): String =
        firebaseMessaging.token.await()
}