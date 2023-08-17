package org.hyperskill.app.notification.remote.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.remote.data.NotificationTimeRepositoryImpl
import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor
import org.hyperskill.app.notification.remote.domain.repository.NotificationTimeRepository
import org.hyperskill.app.notification.remote.remote.NotificationTimeRemoteDataSourceImpl

class PushNotificationsComponentImpl(
    private val appGraph: AppGraph
) : PushNotificationsComponent {

    private val notificationTimeRepository: NotificationTimeRepository =
        NotificationTimeRepositoryImpl(
            NotificationTimeRemoteDataSourceImpl(
                appGraph.networkComponent.authorizedHttpClient
            )
        )

    override val pushNotificationsInteractor: PushNotificationsInteractor
        get() = PushNotificationsInteractor(
            platform = appGraph.commonComponent.platform,
            devicesRepository = appGraph.buildDevicesDataComponent().devicesRepository,
            authInteractor = appGraph.authComponent.authInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            notificationTimeRepository = notificationTimeRepository,
            json = appGraph.commonComponent.json
        )
}