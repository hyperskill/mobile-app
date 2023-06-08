package org.hyperskill.app.push_notifications.domain.interactor

import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.model.DeviceType
import org.hyperskill.app.devices.domain.model.toDeviceType
import org.hyperskill.app.devices.domain.repository.DevicesRepository

class PushNotificationsInteractor(
    platform: Platform,
    private val devicesRepository: DevicesRepository,
    private val authInteractor: AuthInteractor
) {
    private val deviceType: DeviceType =
        platform.platformType.toDeviceType()

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
        }
    }

    internal fun handleUserDeauthorized() {
        devicesRepository.clearCache()
    }

    internal suspend fun handleUserSignedOut() {
        val currentFCMToken = getCurrentFCMToken()
        devicesRepository.clearCache()

        currentFCMToken?.let { disableFCMToken(it) }
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
            .createDevice(registrationId = fcmToken, isActive = true, type = deviceType)
            .onSuccess { devicesRepository.saveDeviceToCache(it) }

    private suspend fun disableFCMToken(fcmToken: String): Result<Device> =
        devicesRepository
            .createDevice(registrationId = fcmToken, isActive = false, type = deviceType)
}