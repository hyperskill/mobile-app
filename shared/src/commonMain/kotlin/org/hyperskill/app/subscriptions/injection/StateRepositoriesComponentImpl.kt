package org.hyperskill.app.subscriptions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.injection.StateRepositoriesComponent
import org.hyperskill.app.subscriptions.data.repository.CurrentSubscriptionStateRepositoryImpl
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class StateRepositoriesComponentImpl(appGraph: AppGraph) : StateRepositoriesComponent {
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        CurrentSubscriptionStateRepositoryImpl(subscriptionsRemoteDataSource)
}