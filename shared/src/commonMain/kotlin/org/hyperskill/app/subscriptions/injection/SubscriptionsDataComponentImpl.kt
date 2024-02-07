package org.hyperskill.app.subscriptions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.subscriptions.data.repository.SubscriptionsRepositoryImpl
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class SubscriptionsDataComponentImpl(
    appGraph: AppGraph
) : SubscriptionsDataComponent {

    private val subscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val subscriptionsRepository: SubscriptionsRepository =
        SubscriptionsRepositoryImpl(subscriptionsRemoteDataSource)
}