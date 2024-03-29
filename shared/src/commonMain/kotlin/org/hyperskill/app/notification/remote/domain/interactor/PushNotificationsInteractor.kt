package org.hyperskill.app.notification.remote.domain.interactor

import kotlinx.serialization.json.Json
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.domain.platform.Platform
import org.hyperskill.app.core.utils.toJsonElement
import org.hyperskill.app.devices.domain.model.Device
import org.hyperskill.app.devices.domain.model.toDeviceType
import org.hyperskill.app.devices.domain.repository.DevicesRepository
import org.hyperskill.app.notification.remote.domain.model.PushNotificationData
import org.hyperskill.app.notification.remote.domain.repository.FCMTokenRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor

class PushNotificationsInteractor(
    private val platform: Platform,
    private val devicesRepository: DevicesRepository,
    private val authInteractor: AuthInteractor,
    private val sentryInteractor: SentryInteractor,
    private val json: Json,
    private val fcmTokenRepository: FCMTokenRepository
) {
    suspend fun handleNewFCMToken(fcmToken: String): Result<Device> {
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

    internal suspend fun renewFCMToken() {
        val token = fcmTokenRepository.getToken()
            .onFailure { e ->
                sentryInteractor.captureErrorMessage("PushNotificationsInteractor failed to fetch token: $e")
            }
            .onSuccess {
                println("RenewFCMToken: token = $it")
            }
            .getOrNull()
        if (token != null) {
            handleNewFCMToken(token)
        }
    }

    internal suspend fun handleUserSignedOut() {
        getCurrentFCMToken()?.let { disableFCMToken(it) }
    }

    fun parsePushNotificationData(rawNotificationData: Map<String, Any>): PushNotificationData? =
        try {
            val notificationJson = rawNotificationData.toJsonElement()
            json.decodeFromJsonElement(
                PushNotificationData.serializer(),
                notificationJson
            )
        } catch (e: Exception) {
            sentryInteractor.captureErrorMessage(
                "Unable to parse PushNotificationData from $rawNotificationData.\n$e"
            )
            null
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