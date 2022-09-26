package org.hyperskill.app.notification.injection

import org.hyperskill.app.notification.domain.interactor.NotificationInteractor

interface NotificationComponent {
    val notificationInteractor: NotificationInteractor
}