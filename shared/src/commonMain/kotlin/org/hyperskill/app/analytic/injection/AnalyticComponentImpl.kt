package org.hyperskill.app.analytic.injection

import kotlinx.coroutines.sync.Mutex
import org.hyperskill.app.analytic.cache.AnalyticHyperskillCacheDataSourceImpl
import org.hyperskill.app.analytic.data.repository.AnalyticHyperskillRepositoryImpl
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillCacheDataSource
import org.hyperskill.app.analytic.data.source.AnalyticHyperskillRemoteDataSource
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.domain.processor.AnalyticHyperskillEventProcessor
import org.hyperskill.app.analytic.domain.repository.AnalyticHyperskillRepository
import org.hyperskill.app.analytic.remote.AnalyticHyperskillRemoteDataSourceImpl
import org.hyperskill.app.auth.injection.AuthComponent
import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.notification.local.injection.NotificationComponent
import org.hyperskill.app.profile.injection.ProfileDataComponent
import org.hyperskill.app.sentry.injection.SentryComponent

class AnalyticComponentImpl(
    networkComponent: NetworkComponent,
    commonComponent: CommonComponent,
    authComponent: AuthComponent,
    profileDataComponent: ProfileDataComponent,
    notificationComponent: NotificationComponent,
    sentryComponent: SentryComponent
) : AnalyticComponent {
    private val hyperskillRemoteDataSource: AnalyticHyperskillRemoteDataSource =
        AnalyticHyperskillRemoteDataSourceImpl(
            networkComponent.authorizedHttpClient,
            networkComponent.frontendEventsUnauthorizedHttpClient
        )
    private val hyperskillCacheDataSource: AnalyticHyperskillCacheDataSource =
        AnalyticHyperskillCacheDataSourceImpl()
    private val hyperskillRepository: AnalyticHyperskillRepository =
        AnalyticHyperskillRepositoryImpl(
            Mutex(),
            hyperskillRemoteDataSource,
            hyperskillCacheDataSource
        )

    private val hyperskillEventProcessor =
        AnalyticHyperskillEventProcessor(commonComponent.platform)

    override val analyticInteractor: AnalyticInteractor =
        AnalyticInteractor(
            authComponent.authInteractor,
            profileDataComponent.currentProfileStateRepository,
            notificationComponent.notificationInteractor,
            hyperskillRepository,
            hyperskillEventProcessor,
            sentryComponent.sentryInteractor
        )
}