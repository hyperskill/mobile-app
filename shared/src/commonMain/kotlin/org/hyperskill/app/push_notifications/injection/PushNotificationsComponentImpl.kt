package org.hyperskill.app.push_notifications.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.push_notifications.domain.interactor.PushNotificationsInteractor

class PushNotificationsComponentImpl(
    private val appGraph: AppGraph
) : PushNotificationsComponent {

    override val pushNotificationsInteractor: PushNotificationsInteractor
        get() = PushNotificationsInteractor(
            platform = appGraph.commonComponent.platform,
            devicesRepository = appGraph.buildDevicesDataComponent().devicesRepository,
            authInteractor = appGraph.authComponent.authInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )
}