package org.hyperskill.app.notification.local.injection

import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor

interface NotificationComponent {
    val notificationInteractor: NotificationInteractor
}