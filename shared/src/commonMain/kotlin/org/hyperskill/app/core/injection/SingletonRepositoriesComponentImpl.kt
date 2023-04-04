package org.hyperskill.app.core.injection

import org.hyperskill.app.progresses.data.repository.ProgressesRepositoryImpl
import org.hyperskill.app.progresses.domain.repository.ProgressesRepository
import org.hyperskill.app.progresses.remote.ProgressesRemoteDataSourceImpl
import org.hyperskill.app.subscriptions.data.repository.CurrentSubscriptionStateRepositoryImpl
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class SingletonRepositoriesComponentImpl(appGraph: AppGraph) : SingletonRepositoriesComponent {

    private val authorizedHttpClient = appGraph.networkComponent.authorizedHttpClient

    /**
     * Current subscription
     */
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(authorizedHttpClient)

    override val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        CurrentSubscriptionStateRepositoryImpl(subscriptionsRemoteDataSource)

    /**
     * Progress
     */
    override val progressesRepository: ProgressesRepository =
        ProgressesRepositoryImpl(ProgressesRemoteDataSourceImpl(authorizedHttpClient))
}