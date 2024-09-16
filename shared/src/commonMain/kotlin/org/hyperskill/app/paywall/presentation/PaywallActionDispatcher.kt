package org.hyperskill.app.paywall.presentation

import co.touchlab.kermit.Logger
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
            is InternalAction.StartSubscriptionProductPurchase ->
                handleStartSubscriptionProductPurchase(action, ::onNewMessage)
            is InternalAction.SyncSubscription ->
                handleSyncSubscription(::onNewMessage)
            is InternalAction.LogWrongSubscriptionTypeAfterSync ->
                handleLogWrongSubscriptionTypeAfterSync(action)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchMobileOnlyPrice(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildPaywallFetchSubscriptionPrice(),
            onError = { e ->
                logger.e(e) { "Failed to load subscription price" }
                InternalMessage.FetchSubscriptionProductsError
            }
        ) {
            val subscriptionProducts = purchaseInteractor
                .getSubscriptionProducts()
                .getOrThrow()

            if (subscriptionProducts.isNotEmpty()) {
                InternalMessage.FetchSubscriptionProductsSuccess(subscriptionProducts)
            } else {
                logger.e { "Receive null instead of formatted mobile-only subscription price" }
                InternalMessage.FetchSubscriptionProductsError
            }
        }.let(onNewMessage)
    }

    private suspend fun handleStartSubscriptionProductPurchase(
        action: InternalAction.StartSubscriptionProductPurchase,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildPaywallFeaturePurchaseSubscription(),
            onError = { e ->
                logger.e(e) { "Failed to purchase subscription" }
                InternalMessage.MobileOnlySubscriptionPurchaseError
            }
        ) {
            val purchaseResult = purchaseInteractor
                .purchaseSubscriptionProduct(
                    storeProduct = action.storeProduct,
                    platformPurchaseParams = action.purchaseParams
                )
                .getOrThrow()

            if (purchaseResult is PurchaseResult.Error) {
                logger.e { getPurchaseErrorMessage(purchaseResult) }
            }

            InternalMessage.MobileOnlySubscriptionPurchaseSuccess(purchaseResult)
        }.let(onNewMessage)
    }

    private fun getPurchaseErrorMessage(error: PurchaseResult.Error): String =
        """
            Subscription purchase failed!
            error message: ${error.message}
            underlying error message: ${error.underlyingErrorMessage}
        """.trimIndent()

    private suspend fun handleSyncSubscription(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildPaywallFeatureSyncSubscription(),
            onError = { e ->
                logger.e(e) { "Failed to sync subscription status" }
                InternalMessage.SubscriptionSyncError
            }
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