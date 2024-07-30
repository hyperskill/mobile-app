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
            .map {
                it.orContentTrial(
                    isMobileContentTrialEnabled = featuresDataSource.getFeaturesMap().isMobileContentTrialEnabled,
                    canMakePayments = canMakePayments()
                )
            }

    override val changes: Flow<Subscription>
        get() = super.changes.map {
            it.orContentTrial(
                isMobileContentTrialEnabled = featuresDataSource.getFeaturesMap().isMobileContentTrialEnabled,
                canMakePayments = canMakePayments()
            )
        }

    private suspend fun canMakePayments(): Boolean =
        purchaseInteractor.canMakePayments().getOrDefault(false)
}