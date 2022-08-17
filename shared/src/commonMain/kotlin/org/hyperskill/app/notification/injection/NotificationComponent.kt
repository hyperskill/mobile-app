package org.hyperskill.app.notification.injection

import org.hyperskill.app.notification.domain.NotificationInteractor

interface NotificationComponent {
    val notificationInteractor: NotificationInteractor
}