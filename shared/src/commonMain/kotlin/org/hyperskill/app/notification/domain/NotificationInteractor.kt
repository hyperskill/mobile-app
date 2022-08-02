package org.hyperskill.app.notification.domain

import org.hyperskill.app.notification.domain.repository.NotificationRepository

class NotificationInteractor(
    private val notificationRepository: NotificationRepository
) {
    suspend fun getNotificationsEnabled(): Boolean =
        notificationRepository.getNotificationsEnabled()

    suspend fun getNotificationsTimestamp(): Int =
        notificationRepository.getNotificationsTimestamp()
}