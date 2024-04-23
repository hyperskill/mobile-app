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
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class PaywallActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val purchaseInteractor: PurchaseInteractor,
    private val subscriptionsRepository: SubscriptionsRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val sentryInteractor: SentryInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchMobileOnlyPrice ->
                handleFetchMobileOnlyPrice(::onNewMessage)
            is InternalAction.StartMobileOnlySubscriptionPurchase ->
                handleStartMobileOnlySubscriptionPurchase(action, ::onNewMessage)
            is InternalAction.SyncSubscription ->
                handleSyncSubscription(::onNewMessage)
            is InternalAction.LogWrongSubscriptionTypeAfterSync ->
                handleLogWrongSubscriptionTypeAfterSync(action)
            is InternalAction.LogAnalyticEvent ->
                action.analyticEvent.forEach {
                    analyticInteractor.logEvent(it)
                }
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchMobileOnlyPrice(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildPaywallFetchSubscriptionPrice(),
            onError = { InternalMessage.FetchMobileOnlyPriceError }
        ) {
            val price = purchaseInteractor
                .getFormattedMobileOnlySubscriptionPrice()
                .getOrThrow()

            if (price != null) {
                InternalMessage.FetchMobileOnlyPriceSuccess(price)
            } else {
                logger.e { "Receive null instead of formatted mobile-only subscription price" }
                InternalMessage.FetchMobileOnlyPriceError
            }
        }.let(onNewMessage)
    }

    private suspend fun handleStartMobileOnlySubscriptionPurchase(
        action: InternalAction.StartMobileOnlySubscriptionPurchase,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildPaywallFeaturePurchaseSubscription(),
            onError = { InternalMessage.MobileOnlySubscriptionPurchaseError }
        ) {
            val purchaseResult = purchaseInteractor
                .purchaseMobileOnlySubscription(action.purchaseParams)
                .getOrThrow()

            if (purchaseResult is PurchaseResult.Error) {
                logger.e { getPurchaseErrorMessage(purchaseResult) }
            }

            InternalMessage.MobileOnlySubscriptionPurchaseSuccess(purchaseResult)
        }.let(onNewMessage)
    }

    private fun getPurchaseErrorMessage(error: PurchaseResult.Error): String =
        "Subscription purchase failed!\n${error.message}\n${error.underlyingErrorMessage}"

    private suspend fun handleSyncSubscription(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildPaywallFeatureSyncSubscription(),
            onError = { InternalMessage.SubscriptionSyncError }
        ) {
            val subscription = subscriptionsRepository.syncSubscription().getOrThrow()
            currentSubscriptionStateRepository.updateState(subscription)
            InternalMessage.SubscriptionSyncSuccess(subscription)
        }.let(onNewMessage)
    }

    private fun handleLogWrongSubscriptionTypeAfterSync(
        action: InternalAction.LogWrongSubscriptionTypeAfterSync
    ) {
        logger.e {
            """
                Wrong subscription type after sync:
                expected=${action.expectedSubscriptionType}
                actual=${action.actualSubscriptionType}
            """.trimIndent()
        }
    }
}