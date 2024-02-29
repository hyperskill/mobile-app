package org.hyperskill.app.manage_subscription.presentation

import co.touchlab.kermit.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalAction
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.InternalMessage
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class ManageSubscriptionActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val purchaseInteractor: PurchaseInteractor,
    private val sentryInteractor: SentryInteractor,
    private val logger: Logger
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        currentSubscriptionStateRepository
            .changes
            .distinctUntilChanged()
            .onEach { subscription ->
                onNewMessage(
                    InternalMessage.SubscriptionChanged(
                        subscription = subscription,
                        manageSubscriptionUrl = purchaseInteractor.getManagementUrl().getOrNull()
                    )
                )
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchSubscription ->
                handleFetchSubscription(::onNewMessage)
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchSubscription(
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            transaction = HyperskillSentryTransactionBuilder.buildManageSubscriptionFeatureFetchSubscription(),
            onError = {
                InternalMessage.FetchSubscriptionError
            }
        ) {
            coroutineScope {
                val subscriptionDeferred = async {
                    currentSubscriptionStateRepository
                        .getState(forceUpdate = true)
                        .onFailure {
                            logger.e(it) { "Failed to load subscription" }
                        }
                }
                val manageSubscriptionUrlDeferred = async {
                    purchaseInteractor.getManagementUrl()
                }

                InternalMessage.FetchSubscriptionSuccess(
                    subscription = subscriptionDeferred.await().getOrThrow(),
                    manageSubscriptionUrl = manageSubscriptionUrlDeferred.await().getOrThrow()
                )
            }
        }.let(onNewMessage)
    }
}