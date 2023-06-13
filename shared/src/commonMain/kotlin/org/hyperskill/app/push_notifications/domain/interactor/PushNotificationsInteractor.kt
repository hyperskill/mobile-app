package org.hyperskill.app.push_notifications.domain.interactor

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.model.toDeviceType
import org.hyperskill.app.devices.domain.repository.DevicesRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

class PushNotificationsInteractor(
    private val platform: Platform,
    private val devicesRepository: DevicesRepository,
    private val authInteractor: AuthInteractor,
    private val sentryInteractor: SentryInteractor
) {
    suspend fun uploadFCMTokenToBackend(fcmToken: String): Result<Device> {
        val isAuthorized = authInteractor.isAuthorized().getOrNull() ?: false
        if (!isAuthorized) {
            return Result.failure(Exception("User is not authorized"))
        }

        val currentFCMToken = getCurrentFCMToken()

        return if (currentFCMToken != null && currentFCMToken != fcmToken) {
            refreshFCMToken(oldToken = currentFCMToken, newToken = fcmToken)
        } else {
            activateFCMToken(fcmToken)
        }.onFailure { sentryInteractor.captureErrorMessage("PushNotificationsInteractor: $it") }
    }

    internal suspend fun handleUserSignedOut() {
        getCurrentFCMToken()?.let { disableFCMToken(it) }
    }

    private fun getCurrentFCMToken(): String? =
        devicesRepository
            .getCurrentCachedDevice()
            .getOrNull()
            ?.registrationId

    private suspend fun refreshFCMToken(oldToken: String, newToken: String): Result<Device> =
        activateFCMToken(newToken)
            .onSuccess { disableFCMToken(oldToken) }

    private suspend fun activateFCMToken(fcmToken: String): Result<Device> =
        devicesRepository
            .createDevice(
                makeCurrentPlatformDevice(
                    registrationId = fcmToken,
                    isActive = true
                )
            )
            .onSuccess { devicesRepository.saveDeviceToCache(it) }

    private suspend fun disableFCMToken(fcmToken: String): Result<Device> =
        devicesRepository
            .createDevice(
                makeCurrentPlatformDevice(
                    registrationId = fcmToken,
                    isActive = false
                )
            )

    private fun makeCurrentPlatformDevice(registrationId: String, isActive: Boolean): Device =
        Device(
            name = platform.platformDescription,
            registrationId = registrationId,
            isActive = isActive,
            type = platform.platformType.toDeviceType()
        )
}