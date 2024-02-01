package org.hyperskill.app.paywall.presentation

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.InternalMessage
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class PaywallActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val purchaseInteractor: PurchaseInteractor,
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
            is InternalAction.SyncSubscription -> {
                TODO("Not implemented")
            }
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
                        logger.e {
                            "Receive null instead of formatter mobile-only subscription price"
                        }
                        InternalMessage.FetchMobileOnlyPriceError
                    }
                },
                onFailure = {
                    logger.e(it) {
                        "Error during mobile-only subscription price fetching"
                    }
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
                onSuccess = InternalMessage::MobileOnlySubscriptionPurchaseSuccess,
                onFailure = {
                    logger.e(it) {
                        "Subscription purchase failed!"
                    }
                    InternalMessage.MobileOnlySubscriptionPurchaseError
                }
            )
            .let(onNewMessage)
    }
}