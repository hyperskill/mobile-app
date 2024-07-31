package org.hyperskill.app.subscriptions.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.auth.domain.interactor.AuthInteractor
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.features.data.source.FeaturesDataSource
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.subscriptions.data.repository.SubscriptionsRepositoryImpl
import org.hyperskill.app.subscriptions.domain.interactor.SubscriptionsInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import org.hyperskill.app.subscriptions.remote.SubscriptionsRemoteDataSourceImpl

class SubscriptionsDataComponentImpl(
    appGraph: AppGraph
) : SubscriptionsDataComponent {

    private val subscriptionsRemoteDataSource =
        SubscriptionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository =
        appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository

    private val currentProfileStateRepository: CurrentProfileStateRepository =
        appGraph.profileDataComponent.currentProfileStateRepository

    private val featuresDataSource: FeaturesDataSource =
        appGraph.profileDataComponent.featuresDataSource

    private val purchaseInteractor: PurchaseInteractor =
        appGraph.buildPurchaseComponent().purchaseInteractor

    private val authInteractor: AuthInteractor =
        appGraph.authComponent.authInteractor

    private val logger: Logger =
        appGraph.loggerComponent.logger

    override val subscriptionsRepository: SubscriptionsRepository =
        SubscriptionsRepositoryImpl(subscriptionsRemoteDataSource)

    override val subscriptionsInteractor: SubscriptionsInteractor by lazy {
        SubscriptionsInteractor(
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            currentProfileStateRepository = currentProfileStateRepository,
            purchaseInteractor = purchaseInteractor,
            authInteractor = authInteractor,
            featuresDataSource = featuresDataSource,
            logger = logger
        )
    }
}