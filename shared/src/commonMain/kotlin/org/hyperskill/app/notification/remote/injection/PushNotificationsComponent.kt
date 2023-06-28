package org.hyperskill.app.notification.remote.injection

import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor

interface PushNotificationsComponent {
    val pushNotificationsInteractor: PushNotificationsInteractor
}