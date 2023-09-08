package org.hyperskill.app.notification.remote.data

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository
import org.hyperskill.app.play_services.domain.PlayServicesChecker

class AndroidFCMTokenRepository(
    private val firebaseMessaging: FirebaseMessaging,
    private val playServicesChecker: PlayServicesChecker
) : FCMTokenRepository {
    override suspend fun getToken(): Result<String?> =
        runCatching {
            if (playServicesChecker.arePlayServicesAvailable()) {
                firebaseMessaging.token.await()
            } else {
                null
            }
        }
}