package org.hyperskill.app.paywall.presentation

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalMessage
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.purchases.domain.model.PurchaseResult
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class PaywallActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val purchaseInteractor: PurchaseInteractor,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticsEvent ->
                analyticInteractor.logEvent(action.event)
            is InternalAction.FetchMobileOnlyPrice ->
                handleFetchMobileOnlyPrice(::onNewMessage)
            is InternalAction.StartMobileOnlySubscriptionPurchase ->
                handleStartMobileOnlySubscriptionPurchase(action, ::onNewMessage)
            is InternalAction.SyncSubscription ->
                handleSyncSubscription(::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchMobileOnlyPrice(
        onNewMessage: (Message) -> Unit
    ) {
        purchaseInteractor.getFormattedMobileOnlySubscriptionPrice()
            .fold(
                onSuccess = { price ->
                    if (price != null) {
                        InternalMessage.FetchMobileOnlyPriceSuccess(price)
                    } else {
                        logger.e { "Receive null instead of formatter mobile-only subscription price" }
                        InternalMessage.FetchMobileOnlyPriceError
                    }
                },
                onFailure = {
                    logger.e(it) { "Error during mobile-only subscription price fetching" }
                    InternalMessage.FetchMobileOnlyPriceError
                }
            )
            .let(onNewMessage)
    }

    private suspend fun handleStartMobileOnlySubscriptionPurchase(
        action: InternalAction.StartMobileOnlySubscriptionPurchase,
        onNewMessage: (Message) -> Unit
    ) {
        purchaseInteractor
            .purchaseMobileOnlySubscription(action.purchaseParams)
            .fold(
                onSuccess = { purchaseResult ->
                    if (purchaseResult is PurchaseResult.Error) {
                        logger.e { getPurchaseErrorMessage(purchaseResult) }
                    }
                    InternalMessage.MobileOnlySubscriptionPurchaseSuccess(purchaseResult)
                },
                onFailure = {
                    logger.e(it) { "Subscription purchase failed!" }
                    InternalMessage.MobileOnlySubscriptionPurchaseError
                }
            )
            .let(onNewMessage)
    }

    private fun getPurchaseErrorMessage(error: PurchaseResult.Error): String =
        "Subscription purchase failed!\n${error.message}\n${error.underlyingErrorMessage}"

    private suspend fun handleSyncSubscription(
        onNewMessage: (Message) -> Unit
    ) {
        subscriptionsRepository
            .syncSubscription()
            .fold(
                onSuccess = InternalMessage::SubscriptionSyncSuccess,
                onFailure = {
                    logger.e(it) { "Failed to sync subscription" }
                    InternalMessage.SubscriptionSyncError
                }
            )
            .let(onNewMessage)
    }
}