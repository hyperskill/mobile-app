package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.cache.AppCacheDataSourceImpl
import org.hyperskill.app.main.data.repository.AppRepositoryImpl
import org.hyperskill.app.main.data.source.AppCacheDataSource
import org.hyperskill.app.main.domain.interactor.AppInteractor
import org.hyperskill.app.main.domain.repository.AppRepository

internal class MainDataComponentImpl(private val appGraph: AppGraph) : MainDataComponent {
    private val appCacheDataSource: AppCacheDataSource =
        AppCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val appRepository: AppRepository =
        AppRepositoryImpl(appCacheDataSource)

    override val appInteractor: AppInteractor
        get() = AppInteractor(
            appRepository = appRepository,
            authInteractor = appGraph.authComponent.authInteractor,
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            userStorageInteractor = appGraph.buildUserStorageComponent().userStorageInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            progressesRepository = appGraph.buildProgressesDataComponent().progressesRepository,
            trackRepository = appGraph.buildTrackDataComponent().trackRepository,
            providersRepository = appGraph.buildProvidersDataComponent().providersRepository,
            projectsRepository = appGraph.buildProjectsDataComponent().projectsRepository,
            shareStreakRepository = appGraph.buildShareStreakDataComponent().shareStreakRepository,
            pushNotificationsInteractor = appGraph.buildPushNotificationsComponent().pushNotificationsInteractor
        )
}