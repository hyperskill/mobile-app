package org.hyperskill.app.push_notifications.injection

import org.hyperskill.app.push_notifications.domain.interactor.PushNotificationsInteractor

interface PushNotificationsComponent {
    val pushNotificationsInteractor: PushNotificationsInteractor
}