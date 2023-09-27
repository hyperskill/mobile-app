package org.hyperskill.app.notification.local.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notification.local.cache.NotificationCacheDataSourceImpl
import org.hyperskill.app.notification.local.data.repository.NotificationRepositoryImpl
import org.hyperskill.app.notification.local.data.source.NotificationCacheDataSource
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification.local.domain.repository.NotificationRepository
import org.hyperskill.app.profile.injection.ProfileDataComponent

class NotificationComponentImpl(appGraph: AppGraph) : NotificationComponent {
    private val notificationCacheDataSource: NotificationCacheDataSource =
        NotificationCacheDataSourceImpl(appGraph.commonComponent.settings, appGraph.commonComponent.resourceProvider)

    private val notificationRepository: NotificationRepository =
        NotificationRepositoryImpl(notificationCacheDataSource)

    private val profileDataComponent: ProfileDataComponent =
        appGraph.profileDataComponent

    override val notificationInteractor: NotificationInteractor =
        NotificationInteractor(
            notificationRepository,
            appGraph.submissionDataComponent.submissionRepository,
            appGraph.notificationFlowDataComponent.dailyStudyRemindersEnabledFlow,
            profileDataComponent.currentProfileStateRepository,
            profileDataComponent.profileRepository
        )
}