package org.hyperskill.app.subscriptions.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.features.data.source.FeaturesDataSource
import org.hyperskill.app.profile.domain.model.isMobileContentTrialEnabled
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.subscriptions.data.source.CurrentSubscriptionStateHolder
import org.hyperskill.app.subscriptions.data.source.SubscriptionsRemoteDataSource
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.orContentTrial
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository

internal class CurrentSubscriptionStateRepositoryImpl(
    private val subscriptionsRemoteDataSource: SubscriptionsRemoteDataSource,
    override val stateHolder: CurrentSubscriptionStateHolder,
    private val featuresDataSource: FeaturesDataSource,
    private val purchaseInteractor: PurchaseInteractor,
) : CurrentSubscriptionStateRepository, BaseStateRepository<Subscription>() {
    override suspend fun loadState(): Result<Subscription> =
        subscriptionsRemoteDataSource
            .getCurrentSubscription()
            .map { mapSubscription(it) }

    override val changes: Flow<Subscription>
        get() = super.changes.map(::mapSubscription)

    private suspend fun mapSubscription(subscription: Subscription): Subscription =
        subscription.orContentTrial(
            isMobileContentTrialEnabled = featuresDataSource.getFeaturesMap().isMobileContentTrialEnabled,
            canMakePayments = purchaseInteractor.canMakePayments().getOrDefault(false)
        )
}